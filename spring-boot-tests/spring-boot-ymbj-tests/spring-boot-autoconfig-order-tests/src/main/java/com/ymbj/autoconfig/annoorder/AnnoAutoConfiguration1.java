package com.ymbj.autoconfig.annoorder;

import com.ymbj.ordertest.autoconfig.annoorderbean.AutoConfigurationAnnoBean1;
import com.ymbj.ordertest.autoconfig.annoorderbean.AutoConfigurationAnnoBean11;
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
public class AnnoAutoConfiguration1 {
	public AnnoAutoConfiguration1() {
		System.out.println("=========annoorder.AnnoAutoConfiguration1 Constructor============");
	}

	@Bean
	public AutoConfigurationAnnoBean1 autoConfigurationAnnoBean1() {
		System.out.println("=========annoorder.AutoConfigurationAnnoBean1============");
		return new AutoConfigurationAnnoBean1();
	}

	@Bean
	public AutoConfigurationAnnoBean11 autoConfigurationAnnoBean11() {
		System.out.println("=========annoorder.AutoConfigurationAnnoBean11============");
		return new AutoConfigurationAnnoBean11();
	}
}
