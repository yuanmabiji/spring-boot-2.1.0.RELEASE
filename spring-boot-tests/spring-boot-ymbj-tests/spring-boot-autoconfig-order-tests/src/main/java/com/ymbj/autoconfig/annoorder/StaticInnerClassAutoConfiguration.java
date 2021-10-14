package com.ymbj.autoconfig.annoorder;

import com.ymbj.ordertest.autoconfig.annoorderbean.*;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
// 结论：
// 在一个外部插件的配置类中：
// 1，静态内部类的bean创建时机总比该配置类的外部bean创建早；
// 2，在同一个配置类文件中，不管是静态内部类配置（InnerConfig）还是外部配置类（StaticInnerClassAutoConfiguration），bean创建顺序总是按照字节码顺序，跟字母顺序无关
// QUETSION 不是自动配置类（springboot能自动扫描的配置类）不知怎样？ANSWER:答案也是跟字母无关，跟字节码顺序有关
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE) // 结论：在拥有静态内部类的配置与普通配置类之间用@AutoConfigureOrder注解也是生效的
@Configuration
public class StaticInnerClassAutoConfiguration {
	public StaticInnerClassAutoConfiguration() {
		System.out.println("=========annoorder.StaticInnerClassAutoConfiguration Constructor============");
	}

	@Bean
	public NoStaticInnerClassBean noStaticInnerClassBean() {
		System.out.println("=========annoorder.NoStaticInnerClassBean============");
		return new NoStaticInnerClassBean();
	}

	@Bean
	public ANoStaticInnerClassBean aNoStaticInnerClassBean() {
		System.out.println("=========annoorder.ANoStaticInnerClassBean============");
		return new ANoStaticInnerClassBean();
	}

	@Configuration
	static class InnerConfig{
		public InnerConfig() {
			System.out.println("=========annoorder.InnerConfig Constructor============");
		}
		@Bean
		public StaticInnerClassBean staticInnerClassBean() {
			System.out.println("=========annoorder.StaticInnerClassBean============");
			return new StaticInnerClassBean();
		}


		@Bean
		public AStaticInnerClassBean aStaticInnerClassBean() {
			System.out.println("=========annoorder.AStaticInnerClassBean============");
			return new AStaticInnerClassBean();
		}
	}
	




}
