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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;

/**
 * Utility to deduce the {@link PropertySources} to use for configuration binding.
 *
 * @author Phillip Webb
 */
class PropertySourcesDeducer {

	private static final Log logger = LogFactory.getLog(PropertySourcesDeducer.class);

	private final ApplicationContext applicationContext;

	PropertySourcesDeducer(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public PropertySources getPropertySources() {
		PropertySourcesPlaceholderConfigurer configurer = getSinglePropertySourcesPlaceholderConfigurer();
		if (configurer != null) { // 若获取的PropertySourcesPlaceholderConfigurer对象不为空，
			return configurer.getAppliedPropertySources(); // 获取属性源，主要用于在ConfigurableListableBeanFactory的后置处理方法postProcessBeanFactory中处理
		}
		MutablePropertySources sources = extractEnvironmentPropertySources();
		if (sources != null) {
			return sources;
		}
		throw new IllegalStateException("Unable to obtain PropertySources from "
				+ "PropertySourcesPlaceholderConfigurer or Environment");
	}

	private MutablePropertySources extractEnvironmentPropertySources() {
		Environment environment = this.applicationContext.getEnvironment();
		if (environment instanceof ConfigurableEnvironment) {
			return ((ConfigurableEnvironment) environment).getPropertySources();
		}
		return null;
	}

	private PropertySourcesPlaceholderConfigurer getSinglePropertySourcesPlaceholderConfigurer() {
		// Take care not to cause early instantiation of all FactoryBeans
		Map<String, PropertySourcesPlaceholderConfigurer> beans = this.applicationContext // // 根据类型获取bean name和其实例，并以bean name作为Map集合的key，bean实例作为map集合的value，返回该Map集合
				.getBeansOfType(PropertySourcesPlaceholderConfigurer.class, false, false);
		if (beans.size() == 1) {
			return beans.values().iterator().next(); // 返回刚才获取的bean实例即PropertySourcesPlaceholderConfigurer对象
		} // 若获取的bean对象有多个，则警告，因为这里获取的是SinglePropertySourcesPlaceholderConfigurer
		if (beans.size() > 1 && logger.isWarnEnabled()) {
			logger.warn(
					"Multiple PropertySourcesPlaceholderConfigurer " + "beans registered "
							+ beans.keySet() + ", falling back to Environment");
		}
		return null;
	}

}
