package com.ymbj.autoconfig;

import com.ymbj.ordertest.AutoConfigurationBean1;
import com.ymbj.ordertest.AutoConfigurationBean2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration2 {
	@Bean
	public AutoConfigurationBean2 autoConfigurationBean2() {
		System.out.println("=========AutoConfigurationBean2============");
		return new AutoConfigurationBean2();
	}
}
