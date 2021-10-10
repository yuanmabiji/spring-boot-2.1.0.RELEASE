package com.ymbj.autoconfig.annoorder;

import com.ymbj.ordertest.autoconfig.annoorder.AutoConfigurationBean1;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 1，AutoConfiguration1，AutoConfiguration2和ZAutoConfiguration跟MainApplication不在同一个package（包括子package），
 * 因此需要通过spring.factories配置方式通过spi方式来加载这些配置类
 * 2，
 */
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@Configuration
public class AutoConfiguration1 {

	@Bean
	public AutoConfigurationBean1 autoConfigurationBean1() {
		System.out.println("=========annoorder.AutoConfigurationBean1============");
		return new AutoConfigurationBean1();
	}
}
