package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.LiteBean2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 结论：
@Configuration/*(proxyBeanMethods = false)*/ // Spring5.2之后增加了proxyBeanMethods这个属性，默认为true，如果设置为false也是Lite模式哈
public class LiteConfiguration3 {



}
