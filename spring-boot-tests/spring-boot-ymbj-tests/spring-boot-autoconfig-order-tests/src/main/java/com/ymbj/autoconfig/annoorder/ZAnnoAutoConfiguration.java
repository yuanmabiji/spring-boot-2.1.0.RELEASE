package com.ymbj.autoconfig.annoorder;

import com.ymbj.ordertest.autoconfig.annoorderbean.ZAutoConfigurationAnnoBean;
import com.ymbj.ordertest.autoconfig.annoorderbean.ZAutoConfigurationAnnoBean2;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 1)
@Configuration
public class ZAnnoAutoConfiguration {
	public ZAnnoAutoConfiguration() {
		System.out.println("=========annoorder.ZAnnoAutoConfiguration Constructor============");
	}

	@DependsOn("autoConfigurationAnnoBean1")
	@Bean
	public ZAutoConfigurationAnnoBean zAutoConfigurationAnnoBean() {
		System.out.println("=========annoorder.ZAutoConfigurationAnnoBean============");
		return new ZAutoConfigurationAnnoBean();
	}

	@Bean
	public ZAutoConfigurationAnnoBean2 zAutoConfigurationAnnoBean2() {
		System.out.println("=========annoorder.ZAutoConfigurationAnnoBean2============");
		return new ZAutoConfigurationAnnoBean2();
	}




}
