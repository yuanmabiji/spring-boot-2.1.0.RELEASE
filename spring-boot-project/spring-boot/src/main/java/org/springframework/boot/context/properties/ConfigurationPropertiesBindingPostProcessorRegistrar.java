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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * {@link ImportBeanDefinitionRegistrar} for binding externalized application properties
 * to {@link ConfigurationProperties} beans.
 *
 * @author Dave Syer
 * @author Phillip Webb
 */
public class ConfigurationPropertiesBindingPostProcessorRegistrar
		implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {
		// 若容器中没有注册ConfigurationPropertiesBindingPostProcessor这个处理属性绑定的后置处理器，
		// 那么将注册ConfigurationPropertiesBindingPostProcessor和ConfigurationBeanFactoryMetadata这两个bean
		// 注意onApplicationEnvironmentPreparedEvent事件加载配置属性在先，然后再注册一些后置处理器用来处理这些配置属性
		if (!registry.containsBeanDefinition(
				ConfigurationPropertiesBindingPostProcessor.BEAN_NAME)) {
			// (1)注册ConfigurationPropertiesBindingPostProcessor后置处理器，用来对配置属性进行后置处理
			registerConfigurationPropertiesBindingPostProcessor(registry);
			// (2)注册一个ConfigurationBeanFactoryMetadata类型的bean，
			// 注意ConfigurationBeanFactoryMetadata实现了BeanFactoryPostProcessor，然后其会在postProcessBeanFactory中注册一些元数据
			registerConfigurationBeanFactoryMetadata(registry);
		}
	}
	// 注册ConfigurationPropertiesBindingPostProcessor后置处理器
	private void registerConfigurationPropertiesBindingPostProcessor(
			BeanDefinitionRegistry registry) {
		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(ConfigurationPropertiesBindingPostProcessor.class);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition(
				ConfigurationPropertiesBindingPostProcessor.BEAN_NAME, definition);

	}
	// 注册ConfigurationBeanFactoryMetadata后置处理器
	private void registerConfigurationBeanFactoryMetadata(
			BeanDefinitionRegistry registry) {
		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(ConfigurationBeanFactoryMetadata.class);
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition(ConfigurationBeanFactoryMetadata.BEAN_NAME,
				definition);
	}

}
