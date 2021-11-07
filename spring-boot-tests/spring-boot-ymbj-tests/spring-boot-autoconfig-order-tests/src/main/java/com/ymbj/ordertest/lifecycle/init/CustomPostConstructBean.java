package com.ymbj.ordertest.lifecycle.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CustomPostConstructBean {

	@PostConstruct
	public void init() {
		System.out.println("==============CustomPostConstructBean.init============");
	}
}
