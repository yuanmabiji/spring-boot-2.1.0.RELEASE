package com.ymbj.componentscanpackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 总结：spring的原型bean的意义是每次getBean都会重新创建一个新的bean实例，但AutowirePrototypeBean作为ComponentScanBean1的一个属性，
//      而ComponentScanBean1又是一个单例bean,因此创建ComponentScanBean1单例bean时就指定了相应的AutowirePrototypeBean实例，因此
//      这里AutowirePrototypeBean作为原型bean是无效果的，需要在testAdd方法中利用application.getBean方法取出才有效果
@Component
public class ComponentScanBean1 {
	/*@Autowired
	private AutowirePrototypeBean autowirePrototypeBean;*/

	private AutowirePrototypeBean autowirePrototypeBean;

	/*@Autowired
	public void setAutowirePrototypeBean(AutowirePrototypeBean autowirePrototypeBean) {
		this.autowirePrototypeBean = autowirePrototypeBean;
	}*/

	@Autowired
	public ComponentScanBean1(AutowirePrototypeBean autowirePrototypeBean) {
		System.out.println("=========ComponentScanBean1(AutowirePrototypeBean autowirePrototypeBean)============");
		this.autowirePrototypeBean = autowirePrototypeBean;
	}

	public ComponentScanBean1() {
		System.out.println("=========ComponentScanBean1============");
	}

	public void testAdd(int x) {
		autowirePrototypeBean.add(x);
	}

	public int getI() {
		return autowirePrototypeBean.getI();
	}
}
