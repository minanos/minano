package com.minano.runtime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan({ "com.minano.runtime.notification" })
@PropertySource({ "classpath:settings.properties" })
public class ContextConfig {
	public ContextConfig() {
		super();
	}

	// beans

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		return pspc;
	}

}
