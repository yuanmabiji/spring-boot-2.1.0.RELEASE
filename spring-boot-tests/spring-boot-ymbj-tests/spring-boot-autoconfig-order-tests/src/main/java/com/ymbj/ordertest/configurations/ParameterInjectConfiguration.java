package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.ConfigurationBean2;
import com.ymbj.ordertest.configurations.beans.MemberBean;
import com.ymbj.ordertest.configurations.beans.ParamMemberBean;
import com.ymbj.ordertest.configurations.beans.ParameterBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 结论：可以通过参数注入的方式来替代直接调用bean方法的方式
@Configuration
public class ParameterInjectConfiguration {
	public ParameterInjectConfiguration() {
		System.out.println("=========configurations.ParameterInjectConfiguration Constructor============");
	}

	@Bean
	public ParameterBean parameterBean(ParamMemberBean paramMemberBean) {
		System.out.println("=========configurations.ParameterBean============");
		// return new ParameterBean(paramMemberBean()); // 可以通过参数注入的方式来替代直接调用bean方法的方式
		return new ParameterBean(paramMemberBean);
	}

	@Bean
	public ParamMemberBean paramMemberBean() {
		System.out.println("=========configurations.ParamMemberBean============");
		return new ParamMemberBean();
	}

}
