package com.jwk.common.redis.exception;

import java.util.concurrent.TimeUnit;
import lombok.Getter;

/**
 * 限流异常
 *
 * @author L.cm
 */
@Getter
public class RateLimiterException extends RuntimeException {
	private final String key;
	private final long max;
	private final long ttl;
	private final TimeUnit timeUnit;

	public RateLimiterException(String key, long max, long ttl, TimeUnit timeUnit) {
		super(String.format("您的访问次数已超限：%s，速率：%d/%ds", key, max, timeUnit.toSeconds(ttl)));
		this.key = key;
		this.max = max;
		this.ttl = ttl;
		this.timeUnit = timeUnit;
	}
}
