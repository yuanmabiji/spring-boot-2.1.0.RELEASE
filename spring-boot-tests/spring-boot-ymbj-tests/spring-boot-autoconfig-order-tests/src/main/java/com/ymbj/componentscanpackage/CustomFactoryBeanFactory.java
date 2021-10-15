package com.ymbj.componentscanpackage;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

// 总结：FactoryBean的getObject方法需要待显示用到CustomFactoryBean这个bean时才会调用哈比如applicationContext.getBean("customFactoryBeanFactory");
@Component
public class CustomFactoryBeanFactory implements FactoryBean<CustomFactoryBean> {
	@Override
	public CustomFactoryBean getObject() throws Exception {
		return new CustomFactoryBean(); // 因为CustomFactoryBean是直接new的，没有加入spring容器管理，所以无法执行其afterProperSet方法
	}

	@Override
	public Class<?> getObjectType() {
		return CustomFactoryBean.class;
	}
}
