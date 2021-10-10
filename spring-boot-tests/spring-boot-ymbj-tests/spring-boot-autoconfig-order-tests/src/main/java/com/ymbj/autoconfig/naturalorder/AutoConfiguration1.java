package com.ymbj.autoconfig.naturalorder;

import com.ymbj.ordertest.autoconfig.naturalorderrbean.AutoConfigurationBean1;
import com.ymbj.ordertest.autoconfig.naturalorderrbean.AutoConfigurationBean3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1，AutoConfiguration1，AutoConfiguration2和ZAutoConfiguration跟MainApplication不在同一个package（包括子package），
 * 因此需要通过spring.factories配置方式通过spi方式来加载这些配置类
 * 2，
 */
@Configuration
public class AutoConfiguration1 {
	public AutoConfiguration1() {
		System.out.println("=========naturalorder.AutoConfiguration1 Constructor============");
	}

	@Bean
	public AutoConfigurationBean3 autoConfigurationBean3() {
		System.out.println("=========naturalorder.AutoConfigurationBean3============");
		return new AutoConfigurationBean3();
	}

	@Bean
	public AutoConfigurationBean1 autoConfigurationBean1() {
		System.out.println("=========naturalorder.AutoConfigurationBean1============");
		return new AutoConfigurationBean1();
	}


}
