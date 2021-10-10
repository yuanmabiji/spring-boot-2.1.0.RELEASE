package com.ymbj.condition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConditionConfig {
	/**
	 * 通过@Conditional 注解，符合windows条件就返回WindowsListService实例
	 *
	 */
	@Bean
	// @Conditional(WindowsCondition.class)
	@ConditionalOnWindows("hello")
	public ListService windonwsListService() {
		return new WindowsListService();
	}
	/**
	 * 通过@Conditional 注解，符合linux条件就返回LinuxListService实例
	 *
	 */
	@Bean
	/**
	 * 在spring容器中，如果同一个类型有多个实例，但我们需要注入一个的时候，我们必须采取措施，不然spring容器
	 * 会报错：....required a single bean, but 2 were found:.........
	 * 有时候我们能保证同一个类型在spring容器中只有一个实例，有时候我们保证不了，此时不讨论by name注入。这
	 * 个时候@Primary注解就非常重要了。
	 */
	@Primary // 因为ConditionTest测试类是根据ListService listService = context.getBean(ListService.class);类型注入，而符合条件的又有两个bean，此时必须定义@Primary注解
	@ConditionalOnLinux(environment = "linux")
	public ListService linuxListService() {
		return new LinuxListService();
	}
}
