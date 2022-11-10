package com.jwk.common.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Jiwk
 * @date 2022/11/10
 * @version 0.1.3
 * <p>
 * redis 限流限流配置
 */
@Data
@ConfigurationProperties(prefix = "jwk.rate-limiter.redis")
public class RedisRateLimiterProperties {

	/**
	 * 是否开启限流组件
	 */
	private boolean enable = false;

	/**
	 * redis 限流 key 前缀
	 */
	private String keyPrefix = "limiter";

	/**
	 * redisScript
	 */
	private String scriptPath = "META-INF/scripts/redis_rate_limiter.lua";

}
