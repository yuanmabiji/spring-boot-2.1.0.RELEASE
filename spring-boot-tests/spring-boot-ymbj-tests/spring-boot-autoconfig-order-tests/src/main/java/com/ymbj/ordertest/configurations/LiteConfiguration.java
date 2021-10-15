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
// 3,applicationContext.getBean("liteBean")或applicationContext.getBean("liteMemberBean")都会直接从容器中获取相应bean，不会再执行一次liteMemberBean方法哈
// 4，Lite模式下：LiteConfiguration的任意方法（包括bean方法）不会被cglib增强代理，调用的就是LiteConfiguration本身的方法；而full模式下，调用的是增强的方法
// 5，可以通过参数注入的方式（参加liteBean2方法）来替代直接调用bean方法的方式，因为在云原生时代，需要缩短项目启动时间，springBoot的很多自动配置类@Configuration都标注了(proxyBeanMethods = false)
//   即使用Lite模式避免配置类被cglib代理，从而缩短项目启动时间。此时就不能直接用bean方法直接调用的注入方式了（因为不会被cglib增强代理，直接bean方法调用会创建多个bean，
//   对于单例bean是不允许的，因为一般情况下都是单利bean），此时就需要用参数注入的方式了
// 6,相对类可以注册不同名称的bean实例到spring容器	，比如liteBean和liteBean2方法
// 		System.out.println(applicationContext.getBean("liteBean"));
//		System.out.println(applicationContext.getBean("liteBean"));
//		System.out.println(applicationContext.getBean("liteBean2"));
//
//		System.out.println(applicationContext.getBeansOfType(LiteBean.class));
//
//		System.out.println(applicationContext.getBean(LiteBean.class)); // 报No qualifying bean of type 'com.ymbj.ordertest.configurations.beans.LiteBean' available
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
		return new LiteBean(liteMemberBean(), liteMemberBean2()); // 注意idea会只能提示：lite模式下：Method annotated with @Bean is called directly. Use dependency injection instead.
	}

	/**
	 * 如果注释掉// @Bean 注解，会报以下错误
	 * arameter 1 of method liteBean2 in com.ymbj.ordertest.configurations.LiteConfiguration required a bean of type 'com.ymbj.ordertest.configurations.beans.LiteMemberBean2' that could not be found.
	 *
	 *
	 * Action:
	 *
	 * Consider defining a bean of type 'com.ymbj.ordertest.configurations.beans.LiteMemberBean2' in your configuration.
	 * @return
	 */
	@Bean
	public LiteMemberBean2 liteMemberBean2() {
		System.out.println("=========configurations.LiteMemberBean2============");
		return new LiteMemberBean2();
	}

	@Bean
	public LiteBean liteBean2(LiteMemberBean liteMemberBean, LiteMemberBean2 liteMemberBean2) {
		System.out.println("=========configurations.liteBean2============");
		return new LiteBean(liteMemberBean, liteMemberBean2);
	}


	@Bean
	public LiteConfiguration2 liteConfiguration2() {
		System.out.println("=========configurations.LiteConfiguration2============");
		return new LiteConfiguration2();
	}

}
