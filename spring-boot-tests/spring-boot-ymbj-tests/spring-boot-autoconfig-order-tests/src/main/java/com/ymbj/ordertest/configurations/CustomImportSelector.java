package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.ImportSelectorBean;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

// @Component // 注意：ImportSelector实现类用@Component注解无效，必须用@Import
public class CustomImportSelector implements ImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{ImportSelectorBean.class.getName()};
	}
}
