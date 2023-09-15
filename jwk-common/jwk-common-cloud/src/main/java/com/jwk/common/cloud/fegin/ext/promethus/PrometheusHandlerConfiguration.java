package com.jwk.common.cloud.fegin.ext.promethus;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import feign.Feign;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * sentinel 配置
 * @date 2022/6/11
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class PrometheusHandlerConfiguration {

	@Bean
	@Scope("prototype")
	@ConditionalOnProperty(prefix = "jwk.prometheus", name = "enabled", havingValue = "true")
	public Feign.Builder feignSentinelBuilder() {
		return PrometheusSentinelFeign.builder();
	}

}
