package com.jwk.common.redis.config;

import com.jwk.common.redis.properties.RedisRateLimiterProperties;
import com.jwk.common.redis.ratelimiter.RedisRateLimiterAspect;
import com.jwk.common.redis.ratelimiter.RedisRateLimiterClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 基于 redis 的限流自动配置
 * @date 2022/11/10
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedisRateLimiterProperties.class)
@ConditionalOnProperty(name = "jwk.rate-limiter.redis.enable", havingValue = "true")
public class RateLimiterAutoConfiguration {

	@SuppressWarnings("unchecked")
	private RedisScript<Long> redisRateLimiterScript(RedisRateLimiterProperties redisRateLimiterProperties) {
		DefaultRedisScript redisScript = new DefaultRedisScript<>();
		redisScript.setScriptSource(
				new ResourceScriptSource(new ClassPathResource(redisRateLimiterProperties.getScriptPath())));
		redisScript.setResultType(Long.class);
		return redisScript;
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisRateLimiterClient redisRateLimiter(
			@Qualifier("stringKeyRedisTemplate") RedisTemplate<String, Object> redisTemplate, Environment environment,
			RedisRateLimiterProperties redisRateLimiterProperties) {
		RedisScript<Long> redisRateLimiterScript = redisRateLimiterScript(redisRateLimiterProperties);
		return new RedisRateLimiterClient(redisTemplate, redisRateLimiterScript, environment,
				redisRateLimiterProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisRateLimiterAspect redisRateLimiterAspect(RedisRateLimiterClient rateLimiterClient) {
		return new RedisRateLimiterAspect(rateLimiterClient);
	}

}
