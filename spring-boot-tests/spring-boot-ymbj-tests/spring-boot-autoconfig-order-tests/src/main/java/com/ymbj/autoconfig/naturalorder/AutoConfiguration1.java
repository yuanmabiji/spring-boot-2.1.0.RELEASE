package com.ymbj.autoconfig;

import com.ymbj.ordertest.AutoConfigurationBean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AutoConfiguration1，AutoConfiguration2和ZAutoConfiguration跟MainApplication不在同一个package（包括子package），
 * 因此需要通过spring.factories配置方式通过spi方式来加载这些配置类
 */
@Configuration
public class AutoConfiguration1 {

	@Bean
	public AutoConfigurationBean1 autoConfigurationBean1() {
		System.out.println("=========AutoConfigurationBean1============");
		return new AutoConfigurationBean1();
	}
}
