package com.ymbj.autoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfig {
	@Bean
	@Profile("dev")
	public ProfileService devProfileService() {
		return new DevProfileService();
	}

	@Bean
	@Profile("test")
	public ProfileService testProfileService() {
		return new TestProfileService();
	}
}
