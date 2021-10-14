package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.XConfigurationBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureBefore(Configuration1.class) // 结论：在spring能扫描到的配置类中使用@AutoConfigureBefore等注解是无效的
@Configuration
public class XConfiguration {

	public XConfiguration() {
		System.out.println("=========configurations.XConfiguration Constructor============");
	}

	@Bean
	public XConfigurationBean xConfigurationBean() {
		System.out.println("=========configurations.XConfigurationBean============");
		return new XConfigurationBean();
	}
}
