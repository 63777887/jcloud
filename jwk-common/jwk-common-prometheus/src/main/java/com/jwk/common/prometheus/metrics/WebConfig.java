package com.jwk.common.prometheus.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final PrometheusMetricsInterceptor prometheusMetricsInterceptor;

	/**
	 * 添加自定义拦截器
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(prometheusMetricsInterceptor).addPathPatterns("/**");
	}

}