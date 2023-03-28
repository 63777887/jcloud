package com.jwk.common.cloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * RestTemplate
 * @date 2022/6/11
 */
@Configuration(proxyBeanMethods = false)
public class RestTemplateConfiguration {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
