package com.ymbj.autoconfig;

import com.ymbj.ordertest.AutoConfigurationBean2;
import com.ymbj.ordertest.ZAutoConfigurationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZAutoConfiguration {
	@Bean
	public ZAutoConfigurationBean zAutoConfigurationBean() {
		System.out.println("=========ZAutoConfigurationBean============");
		return new ZAutoConfigurationBean();
	}
}
