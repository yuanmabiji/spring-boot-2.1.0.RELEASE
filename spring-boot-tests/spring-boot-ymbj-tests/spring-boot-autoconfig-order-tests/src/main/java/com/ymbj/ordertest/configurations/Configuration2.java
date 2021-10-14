package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.ConfigurationBean2;
import com.ymbj.ordertest.configurations.beans.MemberBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

// 结论：
// 1,full模式下，ConfigurationBean2的创建需要调用memberBean方法，此时再次调用memberBean方法会被代理，
//   直接从spring容器取出memberBean即可，这就是和litem模式的区别;

@Configuration
public class Configuration2 {
	public Configuration2() {
		System.out.println("=========configurations.Configuration2 Constructor============");
	}

	@Bean
	public MemberBean memberBean() {
		System.out.println("=========configurations.MemberBean============");
		return new MemberBean();
	}

	@Bean
	public ConfigurationBean2 configurationBean2() {
		System.out.println("=========configurations.ConfigurationBean2============");
		return new ConfigurationBean2(memberBean());
	}


}
