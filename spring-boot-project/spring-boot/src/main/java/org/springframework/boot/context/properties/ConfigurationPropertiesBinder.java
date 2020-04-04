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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.boot.context.properties.bind.handler.NoUnboundElementsBindHandler;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.UnboundElementsSourceFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.PropertySources;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

/**
 * Internal class by the {@link ConfigurationPropertiesBindingPostProcessor} to handle the
 * actual {@link ConfigurationProperties} binding.
 *
 * @author Stephane Nicoll
 * @author Phillip Webb
 */
class ConfigurationPropertiesBinder {

	private final ApplicationContext applicationContext;

	private final PropertySources propertySources;

	private final Validator configurationPropertiesValidator;

	private final boolean jsr303Present;

	private volatile Validator jsr303Validator;

	private volatile Binder binder;

	ConfigurationPropertiesBinder(ApplicationContext applicationContext,
			String validatorBeanName) {
		this.applicationContext = applicationContext;
		// 将applicationContext封装到PropertySourcesDeducer对象中并返回
		this.propertySources = new PropertySourcesDeducer(applicationContext)
				.getPropertySources(); // 获取属性源，主要用于在ConfigurableListableBeanFactory的后置处理方法postProcessBeanFactory中处理
		// 如果没有配置validator的话，这里一般返回的是null
		this.configurationPropertiesValidator = getConfigurationPropertiesValidator(
				applicationContext, validatorBeanName);
		// 检查实现JSR-303规范的bean校验器相关类在classpath中是否存在
		this.jsr303Present = ConfigurationPropertiesJsr303Validator
				.isJsr303Present(applicationContext);
	}

	public void bind(Bindable<?> target) {
		//【1】得到@ConfigurationProperties注解
		ConfigurationProperties annotation = target
				.getAnnotation(ConfigurationProperties.class);
		Assert.state(annotation != null,
				() -> "Missing @ConfigurationProperties on " + target);
		// 【2】得到Validator对象集合，用于属性校验
		List<Validator> validators = getValidators(target);
		// 【3】得到BindHandler对象（默认是IgnoreTopLevelConverterNotFoundBindHandler对象），
		// 用于对ConfigurationProperties注解的ignoreUnknownFields等属性的处理
		BindHandler bindHandler = getBindHandler(annotation, validators);
		// 【4】得到一个Binder对象，并利用其bind方法执行外部属性绑定逻辑
		/********************【主线，重点关注】********************/
		getBinder().bind(annotation.prefix(), target, bindHandler);

	}

	private Validator getConfigurationPropertiesValidator(
			ApplicationContext applicationContext, String validatorBeanName) {
		if (applicationContext.containsBean(validatorBeanName)) {
			return applicationContext.getBean(validatorBeanName, Validator.class);
		}
		return null;
	}

	private List<Validator> getValidators(Bindable<?> target) {
		List<Validator> validators = new ArrayList<>(3);
		if (this.configurationPropertiesValidator != null) {
			validators.add(this.configurationPropertiesValidator);
		}
		if (this.jsr303Present && target.getAnnotation(Validated.class) != null) {
			validators.add(getJsr303Validator());
		}
		if (target.getValue() != null && target.getValue().get() instanceof Validator) {
			validators.add((Validator) target.getValue().get());
		}
		return validators;
	}

	private Validator getJsr303Validator() {
		if (this.jsr303Validator == null) {
			this.jsr303Validator = new ConfigurationPropertiesJsr303Validator(
					this.applicationContext);
		}
		return this.jsr303Validator;
	}
	// 注意BindHandler的设计技巧，应该是责任链模式，非常巧妙，值得借鉴
	private BindHandler getBindHandler(ConfigurationProperties annotation,
			List<Validator> validators) {
		// 新建一个IgnoreTopLevelConverterNotFoundBindHandler对象，这是个默认的BindHandler对象
		BindHandler handler = new IgnoreTopLevelConverterNotFoundBindHandler();
		// 若注解@ConfigurationProperties的ignoreInvalidFields属性设置为true，
		// 则说明可以忽略无效的配置属性例如类型错误，此时新建一个IgnoreErrorsBindHandler对象
		if (annotation.ignoreInvalidFields()) {
			handler = new IgnoreErrorsBindHandler(handler);
		}
		// 若注解@ConfigurationProperties的ignoreUnknownFields属性设置为true，
		// 则说明配置文件配置了一些未知的属性配置，此时新建一个ignoreUnknownFields对象
		if (!annotation.ignoreUnknownFields()) {
			UnboundElementsSourceFilter filter = new UnboundElementsSourceFilter();
			handler = new NoUnboundElementsBindHandler(handler, filter);
		}
		// 如果@Valid注解不为空，则创建一个ValidationBindHandler对象
		if (!validators.isEmpty()) {
			handler = new ValidationBindHandler(handler,
					validators.toArray(new Validator[0]));
		}
		// 遍历获取的ConfigurationPropertiesBindHandlerAdvisor集合，
		// ConfigurationPropertiesBindHandlerAdvisor目前只在测试类中有用到
		for (ConfigurationPropertiesBindHandlerAdvisor advisor : getBindHandlerAdvisors()) {
			// 对handler进一步处理
			handler = advisor.apply(handler);
		}
		// 返回handler
		return handler;
	}

	private List<ConfigurationPropertiesBindHandlerAdvisor> getBindHandlerAdvisors() {
		return this.applicationContext
				.getBeanProvider(ConfigurationPropertiesBindHandlerAdvisor.class)
				.orderedStream().collect(Collectors.toList());
	}

	private Binder getBinder() {
		// Binder是一个能绑定ConfigurationPropertySource的容器对象
		if (this.binder == null) {
			// 新建一个Binder对象，这个binder对象封装了ConfigurationPropertySources，
			// PropertySourcesPlaceholdersResolver，ConversionService和PropertyEditorInitializer对象
			this.binder = new Binder(getConfigurationPropertySources(), // 将PropertySources对象封装成SpringConfigurationPropertySources对象并返回
					getPropertySourcesPlaceholdersResolver(), getConversionService(), // 将PropertySources对象封装成PropertySourcesPlaceholdersResolver对象并返回，从容器中获取到ConversionService对象
					getPropertyEditorInitializer()); // 得到Consumer<PropertyEditorRegistry>对象，这些初始化器用来配置property editors，property editors通常可以用来转换值
		}
		// 返回binder
		return this.binder;
	}

	private Iterable<ConfigurationPropertySource> getConfigurationPropertySources() {
		return ConfigurationPropertySources.from(this.propertySources);
	}

	private PropertySourcesPlaceholdersResolver getPropertySourcesPlaceholdersResolver() {
		return new PropertySourcesPlaceholdersResolver(this.propertySources);
	}

	private ConversionService getConversionService() {
		return new ConversionServiceDeducer(this.applicationContext)
				.getConversionService();
	}

	private Consumer<PropertyEditorRegistry> getPropertyEditorInitializer() {
		if (this.applicationContext instanceof ConfigurableApplicationContext) {
			return ((ConfigurableApplicationContext) this.applicationContext)
					.getBeanFactory()::copyRegisteredEditorsTo;
		}
		return null;
	}

}
