package com.ymbj.ordertest;

import com.ymbj.componentscanpackage.ComponentScanBean1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MainApplication {
	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(MainApplication.class, args);

		ComponentScanBean1 componentScanBean1 = (ComponentScanBean1)applicationContext.getBean("componentScanBean1");
		componentScanBean1.testAdd(10);
		ComponentScanBean1 componentScanBean2 = (ComponentScanBean1)applicationContext.getBean("componentScanBean1");
		componentScanBean2.testAdd(10);
		System.out.println(componentScanBean1.getI());
		System.out.println(componentScanBean2.getI());
		applicationContext.getBean("customFactoryBeanFactory");
	}

}
