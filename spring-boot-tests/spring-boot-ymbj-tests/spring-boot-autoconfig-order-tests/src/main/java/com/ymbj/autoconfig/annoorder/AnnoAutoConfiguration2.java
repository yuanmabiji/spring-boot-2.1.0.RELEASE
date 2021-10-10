package com.ymbj.autoconfig.annoorder;

import com.ymbj.ordertest.autoconfig.annoorderbean.AutoConfigurationAnnoBean2;
import com.ymbj.ordertest.autoconfig.annoorderbean.AutoConfigurationAnnoBean22;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureBefore(ZAnnoAutoConfiguration.class)
@Configuration
public class AnnoAutoConfiguration2 {
	public AnnoAutoConfiguration2() {
		System.out.println("=========annoorder.AnnoAutoConfiguration2 Constructor============");
	}
	@Bean
	public AutoConfigurationAnnoBean2 autoConfigurationAnnoBean2() {
		System.out.println("=========annoorder.AutoConfigurationAnnoBean2============");
		return new AutoConfigurationAnnoBean2();
	}

	@Bean
	public AutoConfigurationAnnoBean22 autoConfigurationAnnoBean22() {
		System.out.println("=========annoorder.AutoConfigurationAnnoBean22============");
		return new AutoConfigurationAnnoBean22();
	}
}
