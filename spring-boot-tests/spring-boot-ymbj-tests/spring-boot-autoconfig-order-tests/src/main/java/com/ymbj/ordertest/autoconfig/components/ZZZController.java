package com.ymbj.ordertest.autoconfig.components;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;

import javax.annotation.Priority;

@Priority(Ordered.HIGHEST_PRECEDENCE) // 注意：@Priority注解并不能影响baan的加载创建顺序
@Controller
public class ZZZController {
	public ZZZController() {
		System.out.println("=========ZZZController============");
	}
}
