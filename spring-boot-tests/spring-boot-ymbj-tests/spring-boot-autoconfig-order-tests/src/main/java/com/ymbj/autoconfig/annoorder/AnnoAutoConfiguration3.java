package com.ymbj.autoconfig.annoorder;

import com.ymbj.ordertest.autoconfig.annoorderbean.AutoConfigurationAnnoBean3;
import com.ymbj.ordertest.autoconfig.annoorderbean.AutoConfigurationAnnoBean33;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * 1，AutoConfiguration1，AutoConfiguration2和ZAutoConfiguration跟MainApplication不在同一个package（包括子package），
 * 因此需要通过spring.factories配置方式通过spi方式来加载这些配置类
 * 2，
 */
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE - 10) // 比AnnoAutoConfiguration1优先级高
@Configuration
public class AnnoAutoConfiguration3 {
	public AnnoAutoConfiguration3() {
		System.out.println("=========annoorder.AnnoAutoConfiguration3 Constructor============");
	}



	@Bean
	public AutoConfigurationAnnoBean33 autoConfigurationAnnoBean33() {
		System.out.println("=========annoorder.AutoConfigurationAnnoBean33============");
		return new AutoConfigurationAnnoBean33();
	}

	@Bean
	public AutoConfigurationAnnoBean3 autoConfigurationAnnoBean3() {
		System.out.println("=========annoorder.AutoConfigurationAnnoBean3============");
		return new AutoConfigurationAnnoBean3();
	}
}
