package com.jwk.common.prometheus.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wanghuainan
 * @date 2021/9/24
 */
@Component
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private PrometheusMetricsInterceptor prometheusMetricsInterceptor;

	/**
	 * 添加自定义拦截器
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(prometheusMetricsInterceptor).addPathPatterns("/**");
	}

}