package com.ymbj.ordertest.customlistener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 自定义一个自定义监听器监听Spring的生命周期事件ContextRefreshedEvent，这里自定义监听器是因为监听Spring的生命周期事件，因此这个监听器是通过@Component方式注入的。
 */
@Component
public class CustomSpringListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("=======I am a cuctom ContextRefreshedEvent listener,I am listening ContextRefreshedEvent=====");
    }
}
