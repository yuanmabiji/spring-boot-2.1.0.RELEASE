package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 结论：
// 1,没有标注@Configuration的配置类（比如标注@Component的配置类）的@Bean方法也会创建bean，这是lite模式，标有@Configuration的配置类是full模式；
// 2，lite模式下，LiteBean的创建需要调用liteMemberBean方法，此时liteMemberBean方法不会被代理（即使liteMemberBean已经存在spring容器）
//    而是直接再次调用一遍liteMemberBean方法
@Component
public class LiteConfiguration {

	public LiteConfiguration() {
		System.out.println("=========configurations.LiteConfiguration Constructor============");
	}

	@Bean
	public LiteMemberBean liteMemberBean() {
		System.out.println("=========configurations.LiteMemberBean============");
		return new LiteMemberBean();
	}

	@Bean
	public LiteBean liteBean() {
		System.out.println("=========configurations.LiteBean============");
		return new LiteBean(liteMemberBean()); // 注意idea会只能提示：lite模式下：Method annotated with @Bean is called directly. Use dependency injection instead.
	}

}
