package com.ymbj.autoconfig.naturalorder;

import com.ymbj.ordertest.autoconfig.naturalorderrbean.AutoConfigurationBean2;
import com.ymbj.ordertest.autoconfig.naturalorderrbean.AutoConfigurationBean22;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration2 {
	public AutoConfiguration2() {
		System.out.println("=========naturalorder.AutoConfiguration2 Constructor============");
	}

	@Bean
	public AutoConfigurationBean2 autoConfigurationBean2() {
		System.out.println("=========naturalorder.AutoConfigurationBean2============");
		return new AutoConfigurationBean2();
	}
	@Bean
	public AutoConfigurationBean22 autoConfigurationBean22() {
		System.out.println("=========naturalorder.AutoConfigurationBean22============");
		return new AutoConfigurationBean22();
	}
}
