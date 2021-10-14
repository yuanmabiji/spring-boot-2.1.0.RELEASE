package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.ConfigurationBean2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@Configuration
public class Configuration2 {
	public Configuration2() {
		System.out.println("=========configurations.Configuration2 Constructor============");
	}

	@Bean
	public ConfigurationBean2 configurationBean2() {
		System.out.println("=========configurations.ConfigurationBean2============");
		return new ConfigurationBean2();
	}
}
