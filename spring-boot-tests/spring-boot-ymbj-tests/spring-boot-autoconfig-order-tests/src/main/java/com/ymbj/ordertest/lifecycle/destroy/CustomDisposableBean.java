package com.ymbj.ordertest.lifecycle.destroy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class CustomDisposableBean implements DisposableBean {
	@Override
	public void destroy() throws Exception {
		System.out.println(Thread.currentThread().getName() + "==============CustomDisposableBean.destroy============");
	}
}
