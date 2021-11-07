package com.ymbj.ordertest.lifecycle.init;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class CustomInitializingBean implements InitializingBean {
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("==============CustomInitializingBean.afterPropertiesSet============");
	}
}
