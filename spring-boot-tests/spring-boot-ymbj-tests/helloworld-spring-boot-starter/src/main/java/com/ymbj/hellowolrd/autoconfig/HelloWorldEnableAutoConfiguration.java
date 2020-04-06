package com.ymbj.hellowolrd.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HelloWorldComponentProperties.class) // 方式一：若HelloWorldComponentProperties没有@Component注解，则使用该注解使自动配置类生效。
@ConditionalOnClass(A.class)
@ConditionalOnProperty(prefix = "hello.world", matchIfMissing = false, value = {"name","age"})
public class HelloWorldEnableAutoConfiguration {

	/*// HelloWorldComponentProperties也可以通过@Autowired方式注入进来
	@Autowired
	private HelloWorldComponentProperties properties;*/

	@Bean
	public HelloWorldComponent helloWorldComponent(HelloWorldComponentProperties properties) {
		return new HelloWorldComponent(properties.getName(), properties.getAge());
	}
}
