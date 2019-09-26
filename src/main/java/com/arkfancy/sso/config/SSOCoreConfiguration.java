package com.arkfancy.sso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.arkfancy.sso.support.interceptor.SSOInterceptor;

@PropertySource("classpath:/application-sso.properties")
public class SSOCoreConfiguration implements WebMvcConfigurer {

	@Value("${sso.interceptor.path-pattern:/**}")
	private String interceptorPath;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// kisso 拦截器配置
		registry.addInterceptor(new SSOInterceptor()).addPathPatterns(interceptorPath);
	}

}
