package com.ymbj.ordertest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MainApplication {
	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(MainApplication.class, args);
		System.out.println("=======================================================================");
		System.out.println(applicationContext.getBean("liteBean"));
		System.out.println(applicationContext.getBean("liteBean"));
		System.out.println("=======================================================================");
		System.out.println(applicationContext.getBean("configurationBean2"));
		System.out.println(applicationContext.getBean("configurationBean2"));

	}

}
