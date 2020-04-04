/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.condition;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionMessage.Style;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * {@link Condition} and {@link AutoConfigurationImportFilter} that checks for the
 * presence or absence of specific classes.
 *
 * @author Phillip Webb
 * @see ConditionalOnClass
 * @see ConditionalOnMissingClass
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class OnClassCondition extends FilteringSpringBootCondition {

	@Override
	protected final ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses,
			AutoConfigurationMetadata autoConfigurationMetadata) {
		// Split the work and perform half in a background thread. Using a single
		// additional thread seems to offer the best performance. More threads make
		// things worse
		// 这里经过测试用两个线程去跑的话性能是最好的，大于两个线程性能反而变差
		int split = autoConfigurationClasses.length / 2;
		// 【1】开启一个新线程去扫描判断已经加载的一半自动配置类
		OutcomesResolver firstHalfResolver = createOutcomesResolver(
				autoConfigurationClasses, 0, split, autoConfigurationMetadata);
		// 【2】这里用主线程去扫描判断已经加载的一半自动配置类
		OutcomesResolver secondHalfResolver = new StandardOutcomesResolver(
				autoConfigurationClasses, split, autoConfigurationClasses.length,
				autoConfigurationMetadata, getBeanClassLoader());
		// 【3】先让主线程去执行解析一半自动配置类是否匹配条件
		ConditionOutcome[] secondHalf = secondHalfResolver.resolveOutcomes();
		// 【4】这里用新开启的线程取解析另一半自动配置类是否匹配
		// 注意为了防止主线程执行过快结束，resolveOutcomes方法里面调用了thread.join()来
		// 让主线程等待新线程执行结束，因为后面要合并两个线程的解析结果
		ConditionOutcome[] firstHalf = firstHalfResolver.resolveOutcomes();
		// 新建一个ConditionOutcome数组来存储自动配置类的筛选结果
		ConditionOutcome[] outcomes = new ConditionOutcome[autoConfigurationClasses.length];
		// 将前面两个线程的筛选结果分别拷贝进outcomes数组
		System.arraycopy(firstHalf, 0, outcomes, 0, firstHalf.length);
		System.arraycopy(secondHalf, 0, outcomes, split, secondHalf.length);
		// 返回自动配置类的筛选结果
		return outcomes;
	}

	private OutcomesResolver createOutcomesResolver(String[] autoConfigurationClasses,
			int start, int end, AutoConfigurationMetadata autoConfigurationMetadata) {
		// 新建一个StandardOutcomesResolver对象
		OutcomesResolver outcomesResolver = new StandardOutcomesResolver(
				autoConfigurationClasses, start, end, autoConfigurationMetadata,
				getBeanClassLoader());
		try {
			// new一个ThreadedOutcomesResolver对象，并将StandardOutcomesResolver类型的outcomesResolver对象作为构造器参数传入
			return new ThreadedOutcomesResolver(outcomesResolver);
		}
		// 若上面开启的线程抛出AccessControlException异常，则返回StandardOutcomesResolver对象
		catch (AccessControlException ex) {
			return outcomesResolver;
		}
	}

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		ClassLoader classLoader = context.getClassLoader();
		ConditionMessage matchMessage = ConditionMessage.empty();
		List<String> onClasses = getCandidates(metadata, ConditionalOnClass.class);
		if (onClasses != null) {
			List<String> missing = filter(onClasses, ClassNameFilter.MISSING,
					classLoader);
			if (!missing.isEmpty()) {
				return ConditionOutcome
						.noMatch(ConditionMessage.forCondition(ConditionalOnClass.class)
								.didNotFind("required class", "required classes")
								.items(Style.QUOTE, missing));
			}
			matchMessage = matchMessage.andCondition(ConditionalOnClass.class)
					.found("required class", "required classes").items(Style.QUOTE,
							filter(onClasses, ClassNameFilter.PRESENT, classLoader));
		}
		List<String> onMissingClasses = getCandidates(metadata,
				ConditionalOnMissingClass.class);
		if (onMissingClasses != null) {
			List<String> present = filter(onMissingClasses, ClassNameFilter.PRESENT,
					classLoader);
			if (!present.isEmpty()) {
				return ConditionOutcome.noMatch(
						ConditionMessage.forCondition(ConditionalOnMissingClass.class)
								.found("unwanted class", "unwanted classes")
								.items(Style.QUOTE, present));
			}
			matchMessage = matchMessage.andCondition(ConditionalOnMissingClass.class)
					.didNotFind("unwanted class", "unwanted classes")
					.items(Style.QUOTE, filter(onMissingClasses, ClassNameFilter.MISSING,
							classLoader));
		}
		return ConditionOutcome.match(matchMessage);
	}

	private List<String> getCandidates(AnnotatedTypeMetadata metadata,
			Class<?> annotationType) {
		MultiValueMap<String, Object> attributes = metadata
				.getAllAnnotationAttributes(annotationType.getName(), true);
		if (attributes == null) {
			return null;
		}
		List<String> candidates = new ArrayList<>();
		addAll(candidates, attributes.get("value"));
		addAll(candidates, attributes.get("name"));
		return candidates;
	}

	private void addAll(List<String> list, List<Object> itemsToAdd) {
		if (itemsToAdd != null) {
			for (Object item : itemsToAdd) {
				Collections.addAll(list, (String[]) item);
			}
		}
	}

	private interface  OutcomesResolver {

		ConditionOutcome[] resolveOutcomes();

	}

	private static final class ThreadedOutcomesResolver implements OutcomesResolver {

		private final Thread thread;

		private volatile ConditionOutcome[] outcomes;

		private ThreadedOutcomesResolver(OutcomesResolver outcomesResolver) {
			// 这里开启一个新的线程，这个线程其实还是利用StandardOutcomesResolver的resolveOutcomes方法
			// 对自动配置类进行解析判断是否匹配
			this.thread = new Thread(
					() -> this.outcomes = outcomesResolver.resolveOutcomes());
			// 开启线程
			this.thread.start();
		}

		@Override
		public ConditionOutcome[] resolveOutcomes() {
			try {
				// 调用子线程的Join方法，让主线程等待
				this.thread.join();
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			// 若子线程结束后，此时返回子线程的解析结果
			return this.outcomes;
		}

	}

	private final class StandardOutcomesResolver implements OutcomesResolver {

		private final String[] autoConfigurationClasses;

		private final int start;

		private final int end;

		private final AutoConfigurationMetadata autoConfigurationMetadata;

		private final ClassLoader beanClassLoader;

		private StandardOutcomesResolver(String[] autoConfigurationClasses, int start,
				int end, AutoConfigurationMetadata autoConfigurationMetadata,
				ClassLoader beanClassLoader) {
			this.autoConfigurationClasses = autoConfigurationClasses;
			this.start = start;
			this.end = end;
			this.autoConfigurationMetadata = autoConfigurationMetadata;
			this.beanClassLoader = beanClassLoader;
		}

		@Override
		public ConditionOutcome[] resolveOutcomes() {
			// 再调用getOutcomes方法来解析
			return getOutcomes(this.autoConfigurationClasses, this.start, this.end,
					this.autoConfigurationMetadata);
		}

		private ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses,
				int start, int end, AutoConfigurationMetadata autoConfigurationMetadata) { // 只要autoConfigurationMetadata没有存储相关自动配置类，那么outcome默认为null，则说明匹配
			ConditionOutcome[] outcomes = new ConditionOutcome[end - start];
			// 遍历每一个自动配置类
			for (int i = start; i < end; i++) {
				String autoConfigurationClass = autoConfigurationClasses[i];
				// TODO 对于autoConfigurationMetadata有个疑问：为何有些自动配置类的条件注解能被加载到autoConfigurationMetadata，而有些又不能，比如自己定义的一个自动配置类HelloWorldEnableAutoConfiguration就没有被存到autoConfigurationMetadata中
				if (autoConfigurationClass != null) {
					// 这里取出注解在AutoConfiguration自动配置类类的@ConditionalOnClass注解的指定类的全限定名，
					// 举个栗子，看下面的KafkaStreamsAnnotationDrivenConfiguration这个自动配置类
					/**
					 * @ConditionalOnClass(StreamsBuilder.class)
					 * class KafkaStreamsAnnotationDrivenConfiguration {
					 * // 省略无关代码
					 * }
					 */
					// 那么取出的就是StreamsBuilder类的全限定名即candidates = org.apache.kafka.streams.StreamsBuilder
					String candidates = autoConfigurationMetadata
							.get(autoConfigurationClass, "ConditionalOnClass"); // 因为这里是处理某个类是否存在于classpath中，所以传入的key是ConditionalOnClass
					// 若自动配置类标有ConditionalOnClass注解且有值，此时调用getOutcome判断是否存在于类路径中
					if (candidates != null) {
						// 拿到自动配置类注解@ConditionalOnClass的值后，再调用getOutcome方法去判断匹配结果,若该类存在于类路径，则getOutcome返回null，否则非null
						/*******************【主线，重点关注】******************/
						outcomes[i - start] = getOutcome(candidates);
					}
				}
			}
			return outcomes;
		}
		// 这里只要outcome记录的是不匹配的情况，只要不为null，则说明不匹配；为null，则说明匹配
		private ConditionOutcome getOutcome(String candidates) {
			// candidates的形式为“org.springframework.boot.autoconfigure.aop.AopAutoConfiguration.ConditionalOnClass=org.aspectj.lang.annotation.Aspect,org.aspectj.lang.reflect.Advice,org.aspectj.weaver.AnnotatedElement”
			try {// 自动配置类上@ConditionalOnClass的值只有一个的话，直接调用getOutcome方法判断是否匹配
				if (!candidates.contains(",")) {
					// 看到因为传入的参数是 ClassNameFilter.MISSING，因此可以猜测这里应该是得到不匹配的结果
					/******************【主线，重点关注】********************/
					return getOutcome(candidates, ClassNameFilter.MISSING,
							this.beanClassLoader);
				}
				// 自动配置类上@ConditionalOnClass的值有多个的话，则遍历每个值（其值以逗号，分隔）
				for (String candidate : StringUtils
						.commaDelimitedListToStringArray(candidates)) {
					ConditionOutcome outcome = getOutcome(candidate,
							ClassNameFilter.MISSING, this.beanClassLoader);
					// 可以看到，这里只要有一个不匹配的话，则返回不匹配结果
					if (outcome != null) {
						return outcome;
					}
				}
			}
			catch (Exception ex) {
				// We'll get another chance later
			}
			return null;
		}

		private ConditionOutcome getOutcome(String className,
				ClassNameFilter classNameFilter, ClassLoader classLoader) {
			// 调用classNameFilter的matches方法来判断`@ConditionalOnClass`指定的类存不存在类路径中
			if (classNameFilter.matches(className, classLoader)) { // 这里调用classNameFilter去判断className是否存在于类路径中，其中ClassNameFilter又分为PRESENT和MISSING两种;目前只看到ClassNameFilter为MISSING的调用情况，所以默认为true的话记录不匹配信息；若传入ClassNameFilter为PRESENT的话，估计还要再写一个else分支
				return ConditionOutcome.noMatch(ConditionMessage
						.forCondition(ConditionalOnClass.class)
						.didNotFind("required class").items(Style.QUOTE, className));
			}
			return null;
		}

	}

}
