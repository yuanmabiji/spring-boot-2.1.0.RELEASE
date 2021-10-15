package com.ymbj.componentscanpackage;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AutowirePrototypeBean {
	int i = 0;

	public void add(int x) {
		i += x;
	}

	public int getI() {
		return i;
	}
}
