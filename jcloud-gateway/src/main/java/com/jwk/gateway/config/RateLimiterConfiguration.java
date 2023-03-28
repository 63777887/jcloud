package com.jwk.gateway.config;

import java.util.Objects;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 路由限流配置
 * @date 2022/6/11
 */
@Configuration(proxyBeanMethods = false)
public class RateLimiterConfiguration {

	/**
	 * Remote addr key resolver key resolver.
	 *
	 * @link {https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory}
	 */
	@Bean
	public KeyResolver remoteAddrKeyResolver() {
		// jwk:获取访问者的ip地址, 通过访问者ip地址进行限流, 限流使用的是Redis中的令牌桶算法
		return exchange -> Mono
				.just(Objects.requireNonNull(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()))
						.getAddress().getHostAddress());
	}

}
