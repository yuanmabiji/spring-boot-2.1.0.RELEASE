package com.ymbj.ordertest.hellowolrd.autoconfig;

public class HelloWorldComponent {

	private String name;

	private int age;


	public HelloWorldComponent(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String sayHelloWorld() {
		return "====================hello world! My name is " + name + ", " + "I'm " + age + "years old.";
	}
}
