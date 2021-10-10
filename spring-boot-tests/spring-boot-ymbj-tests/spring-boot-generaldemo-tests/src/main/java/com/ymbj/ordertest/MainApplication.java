package com.ymbj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MainApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(MainApplication.class, args);
		Environment environment = configurableApplicationContext.getEnvironment();
		String value = environment.getProperty("testAtValue");
		System.out.println(value);
	}
}
