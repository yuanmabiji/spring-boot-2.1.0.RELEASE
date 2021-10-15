package com.ymbj.componentscanpackage;

import org.springframework.beans.factory.InitializingBean;

public class CustomFactoryBean2 implements InitializingBean {
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("===========CustomFactoryBean.afterPropertiesSet============");
	}
}
