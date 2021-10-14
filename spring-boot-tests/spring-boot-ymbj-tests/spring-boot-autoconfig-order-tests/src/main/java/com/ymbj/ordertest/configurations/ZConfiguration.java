package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.ZConfigurationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
// ZConfiguration自然排序比Configuration1在后，但是Order优先级比Configuration1高 TODO QUESTION:目前调试结果看@Order注解并不影响Configuration及其bean的加载创建顺序，但为啥ConfigurationClassPostProcessor的processConfigBeanDefinitions方法还要对@Order进行排序呢？注意，经过调试，@AutoConfigureOrder是有效果的
@Order(1)
@Configuration
public class ZConfiguration {
	public ZConfiguration() {
		System.out.println("=========configurations.ZConfiguration Constructor============");
	}

	@Bean
	public ZConfigurationBean zConfigurationBean() {
		System.out.println("=========configurations.ZConfigurationBean============");
		return new ZConfigurationBean();
	}
}
