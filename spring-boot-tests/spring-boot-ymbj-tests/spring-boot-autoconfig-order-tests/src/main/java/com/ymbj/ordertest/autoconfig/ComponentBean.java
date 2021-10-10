package com.ymbj.ordertest.autoconfig;

import org.springframework.stereotype.Component;

@Component
public class ComponentBean {
	public ComponentBean() {
		System.out.println("=========ComponentBean============");
	}
}
