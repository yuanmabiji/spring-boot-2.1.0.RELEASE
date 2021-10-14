package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

// 结论：
// 在同一个配置类文件中
// 1,不管是静态内部类配置（InnerConfig）还是外部配置类（StaticInnerClassAutoConfiguration），bean创建顺序总是按照字节码顺序，跟字母顺序无关;
// 2，成员类配置内的bean总是先于外部配置类的bean先加载创建，但是外部配置类本身bean的创建先于内部成员配置类bean的加载创建，但静态内部配置类本身bean是先于外部配置类bean的加载创建的，也许这就是区别吧 TODO:插件的外部自动配置类应该也是这个规律，就不验证了，猜测
// 3,TODO 待分析，为何自动配置类多用静态内部类呢？而不用成员内部类呢？目前验证成员内部配置类里面的bean也是优先创建的
@Configuration
public class MemberClassConfiguration {

	public MemberClassConfiguration() {
		System.out.println("=========configurations.MemberClassConfiguration Constructor============");
	}


	@Bean
	public MemberClassBean memberClassBean() {
		System.out.println("=========configurations.MemberClassBean============");
		return new MemberClassBean();
	}

	@Bean
	public AMemberClassBean aMemberClassBean() {
		System.out.println("=========configurations.AMemberClassBean============");
		return new AMemberClassBean();
	}

	@Configuration
	class MemberInnerClassConfiguration {
		public MemberInnerClassConfiguration() {
			System.out.println("=========configurations.MemberInnerClassConfiguration Constructor============");
		}

		@Bean
		public MemberInnerClassBean memberInnerClassBean() {
			System.out.println("=========configurations.MemberInnerClassBean============");
			return new MemberInnerClassBean();
		}


		@Bean
		public AMemberInnerClassBean aMemberInnerClassBean() {
			System.out.println("=========configurations.AMemberInnerClassBean============");
			return new AMemberInnerClassBean();
		}
	}
}
