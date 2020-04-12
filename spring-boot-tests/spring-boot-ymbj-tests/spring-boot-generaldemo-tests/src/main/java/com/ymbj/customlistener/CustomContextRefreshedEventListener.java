package com.ymbj.customlistener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 自定义一个事件监听器监听ContextRefreshedEvent
 */
@Component // 注意若是SpringBoot自定义事件监听器则需要通过SpringBoot的SPI机制来扩展，好像不可以通过@Component来扩展。
public class CustomContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("=======I am a cuctom ContextRefreshedEvent listener,I am listening ContextRefreshedEvent=====");
    }
}
