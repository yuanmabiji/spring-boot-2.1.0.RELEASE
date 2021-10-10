package com.ymbj.autoconfig.naturalorder;

import com.ymbj.ordertest.autoconfig.naturalorderrbean.ZAutoConfigurationBean;
import com.ymbj.ordertest.autoconfig.naturalorderrbean.ZAutoConfigurationBean2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZAutoConfiguration {
	public ZAutoConfiguration() {
		System.out.println("=========naturalorder.ZAutoConfiguration Constructor============");
	}

	@Bean
	public ZAutoConfigurationBean zAutoConfigurationBean() {
		System.out.println("=========naturalorder.ZAutoConfigurationBean============");
		return new ZAutoConfigurationBean();
	}

	@Bean
	public ZAutoConfigurationBean2 zAutoConfigurationBean2() {
		System.out.println("=========naturalorder.ZAutoConfigurationBean2============");
		return new ZAutoConfigurationBean2();
	}
}
