package com.ymbj.autoconfig.annoorder;

import com.ymbj.ordertest.autoconfig.annoorder.AutoConfigurationBean2;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE + 10)
@Configuration
public class AutoConfiguration2 {
	@Bean
	public AutoConfigurationBean2 autoConfigurationBean2() {
		System.out.println("=========annoorder.AutoConfigurationBean2============");
		return new AutoConfigurationBean2();
	}
}
