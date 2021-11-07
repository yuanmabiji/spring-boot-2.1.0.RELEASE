package com.ymbj.ordertest.lifecycle.destroy;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class CustomPreDestroyBean {

	@PreDestroy
	public void destroy(){
		System.out.println(Thread.currentThread().getName() + "==============CustomPreDestroyBean.destroy==============");
	}
}
