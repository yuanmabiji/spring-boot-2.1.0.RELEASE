package com.ymbj.ordertest.autoconfig.components;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Order(Ordered.HIGHEST_PRECEDENCE) // 注意：@Order注解并不能影响baan的加载创建顺序
@Service
public class ZService {
	public ZService() {
		System.out.println("=========ZService============");
	}
}
