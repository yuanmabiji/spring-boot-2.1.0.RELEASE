package com.ymbj.autoconfig;

import com.ymbj.hellowolrd.autoconfig.HelloWorldEnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import java.util.Properties;

@SpringBootApplication
public class MainApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(MainApplication.class, args);
		Environment environment = configurableApplicationContext.getEnvironment();
		String value = environment.getProperty("testAtValue");
		System.out.println(value);
	}
}
