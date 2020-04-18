/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.context.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ErrorHandler;

/**
 * {@link SpringApplicationRunListener} to publish {@link SpringApplicationEvent}s.
 * <p>
 * Uses an internal {@link ApplicationEventMulticaster} for the events that are fired
 * before the context is actually refreshed.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @author Artsiom Yudovin
 */
public class EventPublishingRunListener implements SpringApplicationRunListener, Ordered {

	private final SpringApplication application;

	private final String[] args;
	/**
	 * 拥有一个SimpleApplicationEventMulticaster事件广播器来广播事件
	 */
	private final SimpleApplicationEventMulticaster initialMulticaster;

	public EventPublishingRunListener(SpringApplication application, String[] args) {
		this.application = application;
		this.args = args;
		// 新建一个事件广播器SimpleApplicationEventMulticaster对象
		this.initialMulticaster = new SimpleApplicationEventMulticaster();
		// 遍历在构造SpringApplication对象时从spring.factories配置文件中获取的事件监听器
		for (ApplicationListener<?> listener : application.getListeners()) {
			// 将从spring.factories配置文件中获取的事件监听器们添加到相关集合中缓存起来
			this.initialMulticaster.addApplicationListener(listener);
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}
	// 》》》》》发射【ApplicationStartingEvent】事件
	@Override
	public void starting() {
		// EventPublishingRunListener对象将发布ApplicationStartingEvent这件事情委托给了initialMulticaster对象
		// 调用initialMulticaster的multicastEvent方法来发射ApplicationStartingEvent事件
		this.initialMulticaster.multicastEvent(
				new ApplicationStartingEvent(this.application, this.args));
	}
	// 》》》》》发射【ApplicationEnvironmentPreparedEvent】事件
	@Override
	public void environmentPrepared(ConfigurableEnvironment environment) {
		this.initialMulticaster.multicastEvent(new ApplicationEnvironmentPreparedEvent(
				this.application, this.args, environment));
	}
	// 》》》》》发射【ApplicationContextInitializedEvent】事件
	@Override
	public void contextPrepared(ConfigurableApplicationContext context) {
		this.initialMulticaster.multicastEvent(new ApplicationContextInitializedEvent(
				this.application, this.args, context));
	}
	// 》》》》》发射【ApplicationPreparedEvent】事件
	@Override
	public void contextLoaded(ConfigurableApplicationContext context) {
		for (ApplicationListener<?> listener : this.application.getListeners()) {
			if (listener instanceof ApplicationContextAware) {
				((ApplicationContextAware) listener).setApplicationContext(context);
			}
			context.addApplicationListener(listener);
		}
		this.initialMulticaster.multicastEvent(
				new ApplicationPreparedEvent(this.application, this.args, context));
	}
	// 》》》》》发射【ApplicationStartedEvent】事件
	@Override
	public void started(ConfigurableApplicationContext context) {
		context.publishEvent(
				new ApplicationStartedEvent(this.application, this.args, context));
	}
	// 》》》》》发射【ApplicationReadyEvent】事件
	@Override
	public void running(ConfigurableApplicationContext context) {
		context.publishEvent(
				new ApplicationReadyEvent(this.application, this.args, context));
	}
	// 》》》》》发射【ApplicationFailedEvent】事件
	@Override
	public void failed(ConfigurableApplicationContext context, Throwable exception) {
		ApplicationFailedEvent event = new ApplicationFailedEvent(this.application,
				this.args, context, exception);
		if (context != null && context.isActive()) {
			// Listeners have been registered to the application context so we should
			// use it at this point if we can
			context.publishEvent(event);
		}
		else {
			// An inactive context may not have a multicaster so we use our multicaster to
			// call all of the context's listeners instead
			if (context instanceof AbstractApplicationContext) {
				for (ApplicationListener<?> listener : ((AbstractApplicationContext) context)
						.getApplicationListeners()) {
					this.initialMulticaster.addApplicationListener(listener);
				}
			}
			this.initialMulticaster.setErrorHandler(new LoggingErrorHandler());
			this.initialMulticaster.multicastEvent(event);
		}
	}

	private static class LoggingErrorHandler implements ErrorHandler {

		private static Log logger = LogFactory.getLog(EventPublishingRunListener.class);

		@Override
		public void handleError(Throwable throwable) {
			logger.warn("Error calling ApplicationEventListener", throwable);
		}

	}

}
