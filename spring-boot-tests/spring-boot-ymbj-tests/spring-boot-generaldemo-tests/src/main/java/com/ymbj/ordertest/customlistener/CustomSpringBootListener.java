package com.ymbj.ordertest.customlistener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

/**
 * /自定义一个监听器监听SpringBoot的生命周期事件（注意不是Spring的内置生命周期事件哈）
 * 注意若是SpringBoot自定义事件监听器则需要通过SpringBoot的SPI机制来加载到相应集合，而不是通过@Component来加载。
 */
public class CustomSpringBootListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	/**
	 * Handle an application event.
	 *
	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		System.out.println("===============环境变量已经准备好==================");
	}
}
