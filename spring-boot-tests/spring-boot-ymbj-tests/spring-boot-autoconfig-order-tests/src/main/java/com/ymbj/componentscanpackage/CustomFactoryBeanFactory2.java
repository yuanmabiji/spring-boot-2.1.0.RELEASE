package com.ymbj.componentscanpackage;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

// 总结：
// 1,很多时候FactoryBean实现类实现FactoryBean的同时也会实现InitializingBean，类似本例CustomFactoryBeanFactory2的写法（可参考SqlSessionFactoryBean等）；
// 这可能是想优先创建CustomFactoryBean2实例吧，因为getObject需要等到用到时才会调用
// 2，同一包名下（猜测，不同包名下也是这样），FactoryBean实现类实例比普通的@Component bean实例优先创建
//   TODO :但为啥先创建CustomFactoryBeanFactory2后，然后再创建CoponentScanBean2，再执行CustomFactoryBeanFactory2.afterPropertiesSet方法（
//    为何不是创建CustomFactoryBeanFactory2后就执行该方法），然后再创建ZComponentScanBean？
@Component
public class CustomFactoryBeanFactory2 implements FactoryBean<CustomFactoryBean2>, InitializingBean {

	private CustomFactoryBean2 customFactoryBean2;

	public CustomFactoryBeanFactory2() {
		System.out.println("=========CustomFactoryBeanFactory2============");
	}

	@Override
	public CustomFactoryBean2 getObject() throws Exception {
		if (this.customFactoryBean2 == null) {
			createCustomFactoryBean2();
		}
		return this.customFactoryBean2;
	}

	@Override
	public Class<?> getObjectType() {

		return CustomFactoryBean2.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		createCustomFactoryBean2();
	}

	private void createCustomFactoryBean2() {
		this.customFactoryBean2 = new CustomFactoryBean2();
	}
}
