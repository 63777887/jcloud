package com.jwk.common.redis.ratelimiter;

import cn.hutool.core.text.CharPool;
import com.jwk.common.redis.properties.RedisRateLimiterProperties;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * @author Jiwk
 * @date 2022/11/10
 * @version 0.1.3
 * <p>
 * redis 限流服务
 */
@RequiredArgsConstructor
public class RedisRateLimiterClient implements RateLimiterClient {

	/**
	 * redis 限流 key 前缀
	 */
	private final String REDIS_KEY_PREFIX;

	/**
	 * 失败的默认返回值
	 */
	private final long FAIL_CODE = 0;

	/**
	 * redisTemplate
	 */
	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * redisScript
	 */
	private final RedisScript<Long> script;

	/**
	 * env
	 */
	private final Environment environment;

	public RedisRateLimiterClient(RedisTemplate<String, Object> redisTemplate, RedisScript<Long> script,
			Environment environment, RedisRateLimiterProperties redisRateLimiterProperties) {
		this.REDIS_KEY_PREFIX = redisRateLimiterProperties.getKeyPrefix();
		this.redisTemplate = redisTemplate;
		this.script = script;
		this.environment = environment;
	}

	@Override
	public boolean isAllowed(String key, long max, long ttl, TimeUnit timeUnit) {
		// redis key
		String redisKeyBuilder = REDIS_KEY_PREFIX + CharPool.COLON + getApplicationName(environment) + CharPool.COLON
				+ key;
		List<String> keys = Collections.singletonList(redisKeyBuilder);
		// 毫秒，考虑主从策略和脚本回放机制，这个time由客户端获取传入
		long now = System.currentTimeMillis();
		// 转为毫秒，pexpire
		long ttlMillis = timeUnit.toMillis(ttl);
		// 执行命令
		Long result = this.redisTemplate.execute(this.script, keys, max, ttlMillis, now);
		// 结果为空返回失败
		if (result == null) {
			return false;
		}
		// 判断返回成功
		return result != FAIL_CODE;
	}

	private static String getApplicationName(Environment environment) {
		return environment.getProperty("spring.application.name", "");
	}

}
