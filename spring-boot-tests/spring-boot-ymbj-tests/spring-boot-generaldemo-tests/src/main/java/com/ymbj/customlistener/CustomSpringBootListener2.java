package com.ymbj.customlistener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 通过SpringBoot的SPI机制来加载监听器，但监听的却是Spring的内置生命周期事件
 * 注意这里是通过AbstractApplicationEventMulticaster.getApplicationListener来拿到SpringBoot的SPI加载的监听器的（这里指CustomSpringBootListener2）
 * 即 AbstractApplicationEventMulticaster.defaultRetriever.applicationListeners集合已经存储了SpringBoot的SPI加载的监听器们，
 * 因为CustomSpringBootListener2监听的是ContextRefreshedEvent事件，因此在ServletWebServerApplicationContext.finishRefresh方法调用时会从AbstractApplicationEventMulticaster.defaultRetriever.applicationListeners集合
 * 取出CustomSpringBootListener2监听器，来执行监听逻辑
 */
public class CustomSpringBootListener2 implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 * Handle an application event.
	 *
	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("===============用SpringBoot自定义监听器来监听Spring的生命周期事件==================");
	}
}
