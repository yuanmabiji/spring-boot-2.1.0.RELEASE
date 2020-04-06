package com.ymbj.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(WindowsCondition.class)
public @interface ConditionalOnWindows {
	// 注意@ConditionalOnWindows等价于@Conditional(WindowsCondition.class)

	// 注意使用这个注解的属性value时，比如用@ConditionalOnWindows("windows"),
	// 此时value=windows这个值会被传到WindowsCondition类的match方法里，而
	// match方法参数又有一个AnnotatedTypeMetadata类，可以拿到@ConditionalOnWindows
	// 这个这个注解的所有属性值，然后用于match判断是否匹配
	String[] value();
}


