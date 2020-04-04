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

package org.springframework.boot.context.properties.bind;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.source.ConfigurationProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.ConfigurationPropertyState;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.Assert;

/**
 * A container object which Binds objects from one or more
 * {@link ConfigurationPropertySource ConfigurationPropertySources}.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 * @since 2.0.0
 */
public class Binder {

	private static final Set<Class<?>> NON_BEAN_CLASSES = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList(Object.class, Class.class)));

	private static final List<BeanBinder> BEAN_BINDERS;

	static {
		List<BeanBinder> binders = new ArrayList<>();
		binders.add(new JavaBeanBinder());
		BEAN_BINDERS = Collections.unmodifiableList(binders);
	}

	private final Iterable<ConfigurationPropertySource> sources;

	private final PlaceholdersResolver placeholdersResolver;

	private final ConversionService conversionService;

	private final Consumer<PropertyEditorRegistry> propertyEditorInitializer;

	/**
	 * Create a new {@link Binder} instance for the specified sources. A
	 * {@link DefaultFormattingConversionService} will be used for all conversion.
	 * @param sources the sources used for binding
	 */
	public Binder(ConfigurationPropertySource... sources) {
		this(Arrays.asList(sources), null, null, null);
	}

	/**
	 * Create a new {@link Binder} instance for the specified sources. A
	 * {@link DefaultFormattingConversionService} will be used for all conversion.
	 * @param sources the sources used for binding
	 */
	public Binder(Iterable<ConfigurationPropertySource> sources) {
		this(sources, null, null, null);
	}

	/**
	 * Create a new {@link Binder} instance for the specified sources.
	 * @param sources the sources used for binding
	 * @param placeholdersResolver strategy to resolve any property placeholders
	 */
	public Binder(Iterable<ConfigurationPropertySource> sources,
			PlaceholdersResolver placeholdersResolver) {
		this(sources, placeholdersResolver, null, null);
	}

	/**
	 * Create a new {@link Binder} instance for the specified sources.
	 * @param sources the sources used for binding
	 * @param placeholdersResolver strategy to resolve any property placeholders
	 * @param conversionService the conversion service to convert values (or {@code null}
	 * to use {@link ApplicationConversionService})
	 */
	public Binder(Iterable<ConfigurationPropertySource> sources,
			PlaceholdersResolver placeholdersResolver,
			ConversionService conversionService) {
		this(sources, placeholdersResolver, conversionService, null);
	}

	/**
	 * Create a new {@link Binder} instance for the specified sources.
	 * @param sources the sources used for binding
	 * @param placeholdersResolver strategy to resolve any property placeholders
	 * @param conversionService the conversion service to convert values (or {@code null}
	 * to use {@link ApplicationConversionService})
	 * @param propertyEditorInitializer initializer used to configure the property editors
	 * that can convert values (or {@code null} if no initialization is required). Often
	 * used to call {@link ConfigurableListableBeanFactory#copyRegisteredEditorsTo}.
	 */
	public Binder(Iterable<ConfigurationPropertySource> sources,
			PlaceholdersResolver placeholdersResolver,
			ConversionService conversionService,
			Consumer<PropertyEditorRegistry> propertyEditorInitializer) {
		Assert.notNull(sources, "Sources must not be null");
		this.sources = sources; // 这里将从配置文件application.properties中的读取的配置值赋给Iterable<ConfigurationPropertySource>集合对象sources
		this.placeholdersResolver = (placeholdersResolver != null) ? placeholdersResolver
				: PlaceholdersResolver.NONE;
		this.conversionService = (conversionService != null) ? conversionService
				: ApplicationConversionService.getSharedInstance();
		this.propertyEditorInitializer = propertyEditorInitializer;
	}

	/**
	 * Bind the specified target {@link Class} using this binder's
	 * {@link ConfigurationPropertySource property sources}.
	 * @param name the configuration property name to bind
	 * @param target the target class
	 * @param <T> the bound type
	 * @return the binding result (never {@code null})
	 * @see #bind(ConfigurationPropertyName, Bindable, BindHandler)
	 */
	public <T> BindResult<T> bind(String name, Class<T> target) { // 这个方法被多处调用，比如被AnsiOutputApplicationListener,
		return bind(name, Bindable.of(target));
	}

	/**
	 * Bind the specified target {@link Bindable} using this binder's
	 * {@link ConfigurationPropertySource property sources}.
	 * @param name the configuration property name to bind
	 * @param target the target bindable
	 * @param <T> the bound type
	 * @return the binding result (never {@code null})
	 * @see #bind(ConfigurationPropertyName, Bindable, BindHandler)
	 */
	public <T> BindResult<T> bind(String name, Bindable<T> target) {
		return bind(ConfigurationPropertyName.of(name), target, null);
	}

	/**
	 * Bind the specified target {@link Bindable} using this binder's
	 * {@link ConfigurationPropertySource property sources}.
	 * @param name the configuration property name to bind
	 * @param target the target bindable
	 * @param <T> the bound type
	 * @return the binding result (never {@code null})
	 * @see #bind(ConfigurationPropertyName, Bindable, BindHandler)
	 */
	public <T> BindResult<T> bind(ConfigurationPropertyName name, Bindable<T> target) {
		return bind(name, target, null);
	}

	/**
	 * Bind the specified target {@link Bindable} using this binder's
	 * {@link ConfigurationPropertySource property sources}.
	 * @param name the configuration property name to bind
	 * @param target the target bindable
	 * @param handler the bind handler (may be {@code null})
	 * @param <T> the bound type
	 * @return the binding result (never {@code null})
	 */
	public <T> BindResult<T> bind(String name, Bindable<T> target, BindHandler handler) {
		// ConfigurationPropertyName.of(name)：将name（这里指属性前缀名）封装到ConfigurationPropertyName对象中
		// 将外部配置属性绑定到目标对象target中
		return bind(ConfigurationPropertyName.of(name), target, handler);
	}

	/**
	 * Bind the specified target {@link Bindable} using this binder's
	 * {@link ConfigurationPropertySource property sources}.
	 * @param name the configuration property name to bind
	 * @param target the target bindable
	 * @param handler the bind handler (may be {@code null})
	 * @param <T> the bound type
	 * @return the binding result (never {@code null})
	 */
	public <T> BindResult<T> bind(ConfigurationPropertyName name, Bindable<T> target,
			BindHandler handler) {
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(target, "Target must not be null");
		handler = (handler != null) ? handler : BindHandler.DEFAULT;
		// Context是Binder的内部类，实现了BindContext，Context可以理解为Binder的上下文，可以用来获取binder的属性比如Binder的sources属性
		Context context = new Context();
		// 进行属性绑定，并返回绑定属性后的对象bound，注意bound的对象类型是T，T就是@ConfigurationProperties注解的类比如ServerProperties
		/********【主线，重点关注】************/
		T bound = bind(name, target, handler, context, false);
		// 将刚才返回的bound对象封装到BindResult对象中并返回
		return BindResult.of(bound);
	}

	protected final <T> T bind(ConfigurationPropertyName name, Bindable<T> target,
			BindHandler handler, Context context, boolean allowRecursiveBinding) {
		// 清空Binder的configurationProperty属性值
		context.clearConfigurationProperty();
		try {
			// 【1】调用BindHandler的onStart方法，执行一系列的责任链对象的该方法
			target = handler.onStart(name, target, context);
			if (target == null) {
				return null;
			}// 【2】调用bindObject方法对Bindable对象target的属性进行绑定外部配置的值，并返回赋值给bound对象。
			// 举个栗子：比如设置了server.port=8888,那么该方法最终会调用Binder.bindProperty方法，最终返回的bound的value值为8888
			/************【主线：重点关注】***********/
			Object bound = bindObject(name, target, handler, context,
					allowRecursiveBinding);
			// 【3】封装handleBindResult对象并返回，注意在handleBindResult的构造函数中会调用BindHandler的onSucess，onFinish方法
			return handleBindResult(name, target, handler, context, bound);
		}
		catch (Exception ex) {
			return handleBindError(name, target, handler, context, ex);
		}
	}

	private <T> T handleBindResult(ConfigurationPropertyName name, Bindable<T> target,
								   BindHandler handler, Context context, Object result) throws Exception {
		if (result != null) { // 若result不为null，则说明之前的绑定结果成功
			result = handler.onSuccess(name, target, context, result); // 调用BindHandler的onSucess方法，这个onSucess方法可以对之前绑定的result执行一些额外的校验逻辑
			result = context.getConverter().convert(result, target); // 属性值转换
		}
		handler.onFinish(name, target, context, result); // 不管属性之前是否绑定成功，此时都会调用onFinish方法
		return context.getConverter().convert(result, target); // 之前若result不为null的话，之前已经调用过一次
	}

	private <T> T handleBindError(ConfigurationPropertyName name, Bindable<T> target,
			BindHandler handler, Context context, Exception error) {
		try {
			Object result = handler.onFailure(name, target, context, error);
			return context.getConverter().convert(result, target);
		}
		catch (Exception ex) {
			if (ex instanceof BindException) {
				throw (BindException) ex;
			}
			throw new BindException(name, target, context.getConfigurationProperty(), ex);
		}
	}

	private <T> Object bindObject(ConfigurationPropertyName name, Bindable<T> target,
			BindHandler handler, Context context, boolean allowRecursiveBinding) {
		// 从propertySource中的配置属性，获取ConfigurationProperty对象property即application.properties配置文件中若有相关的配置的话，
		// 那么property将不会为null。举个栗子：假如你在配置文件中配置了spring.profiles.active=dev，那么相应property值为dev；否则为null
		ConfigurationProperty property = findProperty(name, context);
		// 若property为null，则不会执行后续的属性绑定相关逻辑
		if (property == null && containsNoDescendantOf(context.getSources(), name)) {
			// 如果property == null，则返回null
			return null;
		}
		// 根据target类型获取不同的Binder，可以是null（普通的类型一般是Null）,MapBinder,CollectionBinder或ArrayBinder
		AggregateBinder<?> aggregateBinder = getAggregateBinder(target, context);
		// 若aggregateBinder不为null比如配置了spring.profiles属性（当然包括其子属性比如spring.profiles.active等）
		if (aggregateBinder != null) {
			// 若aggregateBinder不为null，则调用bindAggregate并返回绑定后的对象
			return bindAggregate(name, target, handler, context, aggregateBinder);
		}
		// 若property不为null
		if (property != null) {
			try {
				// 绑定属性到对象中，比如配置文件中设置了server.port=8888，那么将会最终调用bindProperty方法进行属性设置
				return bindProperty(target, context, property);
			}
			catch (ConverterNotFoundException ex) {
				// We might still be able to bind it as a bean
				Object bean = bindBean(name, target, handler, context,
						allowRecursiveBinding);
				if (bean != null) {
					return bean;
				}
				throw ex;
			}
		}
		// 只有@ConfigurationProperties注解的类进行外部属性绑定才会走这里
		/***********************【主线，重点关注】****************************/
		return bindBean(name, target, handler, context, allowRecursiveBinding);
	}

	private AggregateBinder<?> getAggregateBinder(Bindable<?> target, Context context) {
		Class<?> resolvedType = target.getType().resolve(Object.class); // 获得target对象是哪种类型
		if (Map.class.isAssignableFrom(resolvedType)) { // 若是Map集合类型，则返回MapBinder
			return new MapBinder(context);
		}
		if (Collection.class.isAssignableFrom(resolvedType)) { // 若是Collection集合类型，则返回CollectionBinder
			return new CollectionBinder(context);
		}
		if (target.getType().isArray()) { // 若是数组类型，则返回ArrayBinder，比如是spring.profiles属性的话，那么则说明是数组类型
			return new ArrayBinder(context);
		}
		return null;
	}

	private <T> Object bindAggregate(ConfigurationPropertyName name, Bindable<T> target,
			BindHandler handler, Context context, AggregateBinder<?> aggregateBinder) {
		AggregateElementBinder elementBinder = (itemName, itemTarget, source) -> {
			boolean allowRecursiveBinding = aggregateBinder
					.isAllowRecursiveBinding(source);
			Supplier<?> supplier = () -> bind(itemName, itemTarget, handler, context,
					allowRecursiveBinding);
			return context.withSource(source, supplier);
		};
		return context.withIncreasedDepth(
				() -> aggregateBinder.bind(name, target, elementBinder));
	}
	// 配置属性值将会从这里获取到，最后绑定到相应对象中
	private ConfigurationProperty findProperty(ConfigurationPropertyName name,
			Context context) {
		if (name.isEmpty()) {
			return null;
		}
		for (ConfigurationPropertySource source : context.getSources()) {
			ConfigurationProperty property = source.getConfigurationProperty(name);
			if (property != null) {
				return property;
			}
		}
		return null;
	}

	private <T> Object bindProperty(Bindable<T> target, Context context,
			ConfigurationProperty property) {
		context.setConfigurationProperty(property);
		Object result = property.getValue(); // 得到配置的某个属性值，比如server.port=8888,那么result=8888
		result = this.placeholdersResolver.resolvePlaceholders(result); // 这里针对占位符(${})的情况处理
		result = context.getConverter().convert(result, target);
		return result;
	}

	private Object bindBean(ConfigurationPropertyName name, Bindable<?> target, // name指的是ConfigurationProperties的前缀名
			BindHandler handler, Context context, boolean allowRecursiveBinding) {
		// 这里做一些ConfigurationPropertyState的相关检查
		if (containsNoDescendantOf(context.getSources(), name)
				|| isUnbindableBean(name, target, context)) {
			return null;
		}// 这里新建一个BeanPropertyBinder的实现类对象，注意这个对象实现了bindProperty方法
		BeanPropertyBinder propertyBinder = (propertyName, propertyTarget) -> bind(
				name.append(propertyName), propertyTarget, handler, context, false);
		/**
		 * (propertyName, propertyTarget) -> bind(
		 * 				name.append(propertyName), propertyTarget, handler, context, false);
		 * 	等价于
		 * 	new BeanPropertyBinder() {
		 *		Object bindProperty(String propertyName, Bindable<?> target){
		 *			bind(name.append(propertyName), propertyTarget, handler, context, false);
		 *		}
		 * 	}
		 */
		// type类型即@ConfigurationProperties注解标注的XxxProperties类
		Class<?> type = target.getType().resolve(Object.class);
		if (!allowRecursiveBinding && context.hasBoundBean(type)) {
			return null;
		}
		// 这里应用了java8的lambda语法，作为没怎么学习java8的lambda语法的我，不怎么好理解下面的逻辑，哈哈
		// 【主线】真正实现将外部配置属性绑定到@ConfigurationProperties注解的XxxProperties类的属性中的逻辑应该就是在这句lambda代码了
		return context.withBean(type, () -> {
			Stream<?> boundBeans = BEAN_BINDERS.stream()
					.map((b) -> b.bind(name, target, context, propertyBinder));
			return boundBeans.filter(Objects::nonNull).findFirst().orElse(null);
		});
		// 根据上面的lambda语句翻译如下：
		/** 这里的T指的是各种属性绑定对象，比如ServerProperties
		 * return context.withBean(type, new Supplier<T>() {
		 * 	T get() {
		 * 		Stream<?> boundBeans = BEAN_BINDERS.stream()
		 * 					.map((b) -> b.bind(name, target, context, propertyBinder));
		 * 			return boundBeans.filter(Objects::nonNull).findFirst().orElse(null);
		 *        }
		 *  });
		 */
	}

	private boolean isUnbindableBean(ConfigurationPropertyName name, Bindable<?> target,
			Context context) {
		for (ConfigurationPropertySource source : context.getSources()) {
			if (source.containsDescendantOf(name) == ConfigurationPropertyState.PRESENT) {
				// We know there are properties to bind so we can't bypass anything
				return false;
			}
		}
		Class<?> resolved = target.getType().resolve(Object.class);
		if (resolved.isPrimitive() || NON_BEAN_CLASSES.contains(resolved)) {
			return true;
		}
		return resolved.getName().startsWith("java.");
	}

	private boolean containsNoDescendantOf(Iterable<ConfigurationPropertySource> sources,
			ConfigurationPropertyName name) {
		for (ConfigurationPropertySource source : sources) {
			if (source.containsDescendantOf(name) != ConfigurationPropertyState.ABSENT) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Create a new {@link Binder} instance from the specified environment.
	 * @param environment the environment source (must have attached
	 * {@link ConfigurationPropertySources})
	 * @return a {@link Binder} instance
	 */
	public static Binder get(Environment environment) {
		return new Binder(ConfigurationPropertySources.get(environment),
				new PropertySourcesPlaceholdersResolver(environment));
	}

	/**
	 * Context used when binding and the {@link BindContext} implementation.
	 */
	final class Context implements BindContext {

		private final BindConverter converter;

		private int depth;

		private final List<ConfigurationPropertySource> source = Arrays
				.asList((ConfigurationPropertySource) null);

		private int sourcePushCount;

		private final Deque<Class<?>> beans = new ArrayDeque<>();

		private ConfigurationProperty configurationProperty;

		Context() {
			this.converter = BindConverter.get(Binder.this.conversionService,
					Binder.this.propertyEditorInitializer);
		}

		private void increaseDepth() {
			this.depth++;
		}

		private void decreaseDepth() {
			this.depth--;
		}

		private <T> T withSource(ConfigurationPropertySource source,
				Supplier<T> supplier) {
			if (source == null) {
				return supplier.get();
			}
			this.source.set(0, source);
			this.sourcePushCount++;
			try {
				return supplier.get();
			}
			finally {
				this.sourcePushCount--;
			}
		}

		private <T> T withBean(Class<?> bean, Supplier<T> supplier) {
			this.beans.push(bean);
			try {
				return withIncreasedDepth(supplier);
			}
			finally {
				this.beans.pop();
			}
		}

		private boolean hasBoundBean(Class<?> bean) {
			return this.beans.contains(bean);
		}

		private <T> T withIncreasedDepth(Supplier<T> supplier) {
			increaseDepth();
			try {
				return supplier.get();
			}
			finally {
				decreaseDepth();
			}
		}

		private void setConfigurationProperty(
				ConfigurationProperty configurationProperty) {
			this.configurationProperty = configurationProperty;
		}

		private void clearConfigurationProperty() {
			this.configurationProperty = null;
		}

		public PlaceholdersResolver getPlaceholdersResolver() {
			return Binder.this.placeholdersResolver;
		}

		public BindConverter getConverter() {
			return this.converter;
		}

		@Override
		public Binder getBinder() {
			return Binder.this;
		}

		@Override
		public int getDepth() {
			return this.depth;
		}

		@Override
		public Iterable<ConfigurationPropertySource> getSources() {
			if (this.sourcePushCount > 0) {
				return this.source;
			}
			return Binder.this.sources;
		}

		@Override
		public ConfigurationProperty getConfigurationProperty() {
			return this.configurationProperty;
		}

	}

}
