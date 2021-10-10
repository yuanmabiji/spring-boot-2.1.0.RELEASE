package com.ymbj.ordertest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfigurationInScan {

	@Bean
	public AutoConfigurationBean1 autoConfigurationBean1() {
		System.out.println("=========AutoConfigurationBean1============");
		return new AutoConfigurationBean1();
	}
}
