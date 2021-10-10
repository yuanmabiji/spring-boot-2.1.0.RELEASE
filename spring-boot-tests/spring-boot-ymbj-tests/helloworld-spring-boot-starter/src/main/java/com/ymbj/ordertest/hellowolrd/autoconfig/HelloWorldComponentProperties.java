package com.ymbj.hellowolrd.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// @Component // 方式二：若XXXAutoConfiguration自动配置类中没有@EnableConfigurationProperties(HelloWorldComponentProperties.class)注解，那么在这个类使用@Component注解也一样使自动配置类生效。
@ConfigurationProperties(prefix = "hello.world")
public class HelloWorldComponentProperties {
	private static final String NAME = "ymbj";

	private String name = NAME;

	private int age;


	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
}
