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

package org.springframework.boot.context.properties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Utility class to memorize {@code @Bean} definition meta data during initialization of
 * the bean factory.
 *
 * @author Dave Syer
 * @since 1.1.0
 */
public class ConfigurationBeanFactoryMetadata implements BeanFactoryPostProcessor {

		/**
		 * The bean name that this class is registered with.
		 */
		public static final String BEAN_NAME = ConfigurationBeanFactoryMetadata.class
				.getName();

		private ConfigurableListableBeanFactory beanFactory;
		/**
		 * beansFactoryMetadata集合存储beansFactory的元数据
		 * key:某个bean的名字  value：FactoryMetadata对象（封装了工厂bean名和工厂方法名）
		 * 比如下面这个配置类：
		 *
		 * @Configuration
		 * public class ConfigA {
		 * @Bean
		 * public BeanXXX methodB（configA, ） {
		 *
		 * }
		 * }
		 *
		 * 那么：key为methodB，value为FactoryMetadata（configA, methodB）
		 */
		private final Map<String, FactoryMetadata> beansFactoryMetadata = new HashMap<>();

		@Override
		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
				throws BeansException {
			this.beanFactory = beanFactory;
			// 遍历beanFactory的beanDefinitionName，即每个bean的名字（比如工厂方法对应的bean名字）
			for (String name : beanFactory.getBeanDefinitionNames()) {
				// 根据name得到beanDefinition
				BeanDefinition definition = beanFactory.getBeanDefinition(name);
				// 工厂方法名：一般是注解@Bean的方法名
				String method = definition.getFactoryMethodName();
				// 工厂bean名：一般是注解@Configuration的类名
				String bean = definition.getFactoryBeanName();
				if (method != null && bean != null) {
					// 将beanDefinitionName作为Key，封装了工厂bean名和工厂方法名的FactoryMetadata对象作为value装入beansFactoryMetadata中
					this.beansFactoryMetadata.put(name, new FactoryMetadata(bean, method));
				}
			}
		}

	public <A extends Annotation> Map<String, Object> getBeansWithFactoryAnnotation(
			Class<A> type) {
		Map<String, Object> result = new HashMap<>();
		for (String name : this.beansFactoryMetadata.keySet()) {
			if (findFactoryAnnotation(name, type) != null) {
				result.put(name, this.beanFactory.getBean(name));
			}
		}
		return result;
	}

	public <A extends Annotation> A findFactoryAnnotation(String beanName,
			Class<A> type) {
		Method method = findFactoryMethod(beanName);
		return (method != null) ? AnnotationUtils.findAnnotation(method, type) : null;
	}

	public Method findFactoryMethod(String beanName) {
		if (!this.beansFactoryMetadata.containsKey(beanName)) {
			return null;
		}
		AtomicReference<Method> found = new AtomicReference<>(null);
		FactoryMetadata metadata = this.beansFactoryMetadata.get(beanName);
		Class<?> factoryType = this.beanFactory.getType(metadata.getBean());
		String factoryMethod = metadata.getMethod();
		if (ClassUtils.isCglibProxyClass(factoryType)) {
			factoryType = factoryType.getSuperclass();
		}
		ReflectionUtils.doWithMethods(factoryType, (method) -> {
			if (method.getName().equals(factoryMethod)) {
				found.compareAndSet(null, method);
			}
		});
		return found.get();
	}

	private static class FactoryMetadata {
		// @Configuration注解的配置类的类名
		private final String bean;
		// @Bean注解的方法名
		private final String method;

		FactoryMetadata(String bean, String method) {
			this.bean = bean;
			this.method = method;
		}

		public String getBean() {
			return this.bean;
		}

		public String getMethod() {
			return this.method;
		}

	}

}
