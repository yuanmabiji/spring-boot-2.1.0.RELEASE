package com.ymbj.ordertest.configurations;

import com.ymbj.ordertest.configurations.beans.DeferredImportSelectorBean;
import com.ymbj.ordertest.configurations.beans.ImportSelectorBean;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

// @Component // 注意：DeferredImportSelector实现类用@Component注解无效，必须用@Import
public class CustomDeferredImportSelector implements DeferredImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{DeferredImportSelectorBean.class.getName()};
	}
}
