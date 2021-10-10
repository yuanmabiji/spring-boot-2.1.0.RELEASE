package com.ymbj.ordertest.autoconfig.components;

import com.ymbj.ordertest.autoconfig.annoorderbean.AutoConfigurationAnnoBean11;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

// 【结论】项目内若优先需要创建插件类的某个bean，可以利用项目内的某个bean @DependsOn 插件内的某个bean，这样插件内的某个bean就会优先创建啦
@DependsOn("autoConfigurationAnnoBean11")
@Service
public class AService2 {
	/*@Autowired
	private AutoConfigurationAnnoBean11 autoConfigurationAnnoBean11;*/

	public AService2() {
		System.out.println("=========AService2============");
	}
}
