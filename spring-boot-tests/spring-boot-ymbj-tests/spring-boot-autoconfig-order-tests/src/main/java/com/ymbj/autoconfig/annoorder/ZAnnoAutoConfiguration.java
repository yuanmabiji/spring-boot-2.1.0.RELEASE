package com.ymbj.autoconfig.annoorder;

import com.ymbj.ordertest.autoconfig.annoorder.ZAutoConfigurationBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class ZAutoConfiguration {
	@Bean
	public ZAutoConfigurationBean zAutoConfigurationBean() {
		System.out.println("=========annoorder.ZAutoConfigurationBean============");
		return new ZAutoConfigurationBean();
	}
}
