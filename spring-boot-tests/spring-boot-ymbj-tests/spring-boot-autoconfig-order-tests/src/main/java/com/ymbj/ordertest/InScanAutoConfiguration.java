package com.ymbj.ordertest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * InScanAutoConfiguration在MainApplication同一个package下，因此可以被扫描到
 */
@ComponentScan("com.ymbj.componentscanpackage2")
@Configuration
public class InScanAutoConfiguration {

	public InScanAutoConfiguration() {
		System.out.println("=========InScanAutoConfiguration============");
	}

	@Bean
	public InScanAutoConfigurationBean inScanAutoConfigurationBean() {
		System.out.println("=========InScanAutoConfigurationBean============");
		return new InScanAutoConfigurationBean();
	}
}
