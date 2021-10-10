package com.ymbj.ordertest.autoconfig.components;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@ComponentScan("com.ymbj.componentscanpackage")
@Service
public class AService1 {
	public AService1() {
		System.out.println("=========AService1============");
	}
}
