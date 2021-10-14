package com.ymbj.autoconfig.annoorder;

import com.ymbj.ordertest.autoconfig.annoorderbean.NoStaticInnerClassBean;
import com.ymbj.ordertest.autoconfig.annoorderbean.StaticInnerClassBean;
import com.ymbj.ordertest.autoconfig.annoorderbean.ZNoStaticInnerClassBean;
import com.ymbj.ordertest.autoconfig.annoorderbean.ZStaticInnerClassBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureBefore(StaticInnerClassAutoConfiguration.class) // 结论：AutoConfigureBefore对于有内部静态类的配置类是有效的
@Configuration
public class ZStaticInnerClassAutoConfiguration {
	public ZStaticInnerClassAutoConfiguration() {
		System.out.println("=========annoorder.ZStaticInnerClassAutoConfiguration Constructor============");
	}

	@Bean
	public ZNoStaticInnerClassBean zNoStaticInnerClassBean() {
		System.out.println("=========annoorder.ZNoStaticInnerClassBean============");
		return new ZNoStaticInnerClassBean();
	}

	@Configuration
	static class ZInnerConfig{
		public ZInnerConfig() {
			System.out.println("=========annoorder.ZInnerConfig Constructor============");
		}
		@Bean
		public ZStaticInnerClassBean zStaticInnerClassBean() {
			System.out.println("=========annoorder.ZStaticInnerClassBean============");
			return new ZStaticInnerClassBean();
		}
	}
	




}
