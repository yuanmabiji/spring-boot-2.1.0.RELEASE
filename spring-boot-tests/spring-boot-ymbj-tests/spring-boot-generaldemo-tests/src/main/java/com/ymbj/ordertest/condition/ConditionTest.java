package com.ymbj.ordertest.condition;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 该例子来源于 https://www.cnblogs.com/sam-uncle/p/9111281.html
 */
public class ConditionTest {
	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConditionConfig.class);
		ListService listService = context.getBean(ListService.class);
		System.out.println(context.getEnvironment().getProperty("os.name")
				+ " 系统下的列表命令为： " + listService.showListLine());
	}
}
