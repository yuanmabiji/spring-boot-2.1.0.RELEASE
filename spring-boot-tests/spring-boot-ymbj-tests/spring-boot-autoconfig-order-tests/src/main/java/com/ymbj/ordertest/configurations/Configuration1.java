package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.autoconfig.annoorderbean.AStaticInnerClassBean;
import com.ymbj.ordertest.autoconfig.annoorderbean.StaticInnerClassBean;
import com.ymbj.ordertest.autoconfig.annoorderbean.XAutoConfigurationAnnoBean;
import com.ymbj.ordertest.configurations.beans.AConfigurationBean1;
import com.ymbj.ordertest.configurations.beans.AInnerClassBean;
import com.ymbj.ordertest.configurations.beans.ConfigurationBean1;
import com.ymbj.ordertest.configurations.beans.InnerClassBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
// 结论：在同一个配置类文件中，不管是静态内部类配置（InnerConfig）还是外部配置类（StaticInnerClassAutoConfiguration），bean创建顺序总是按照字节码顺序，跟字母顺序无关
@Order(2)
@Configuration
public class Configuration1 {

	public Configuration1() {
		System.out.println("=========configurations.Configuration1 Constructor============");
	}


	@Bean
	public ConfigurationBean1 configurationBean1() {
		System.out.println("=========configurations.ConfigurationBean1============");
		return new ConfigurationBean1();
	}

	@Bean
	public AConfigurationBean1 aConfigurationBean1() {
		System.out.println("=========configurations.AConfigurationBean1============");
		return new AConfigurationBean1();
	}

	@Configuration
	static class ConfigurationInnerConfig {
		public ConfigurationInnerConfig() {
			System.out.println("=========configurations.ConfigurationInnerConfig Constructor============");
		}

		@Bean
		public InnerClassBean innerClassBean() {
			System.out.println("=========configurations.InnerClassBean============");
			return new InnerClassBean();
		}


		@Bean
		public AInnerClassBean aInnerClassBean() {
			System.out.println("=========configurations.AInnerClassBean============");
			return new AInnerClassBean();
		}
	}
}
