package com.jwk.common.redis.ratelimiter;

import com.jwk.common.redis.exception.RateLimiterException;
import io.reactivex.rxjava3.functions.Supplier;
import java.util.concurrent.TimeUnit;

/**
 * @author Jiwk
 * @date 2022/11/10
 * @version 0.1.3
 * <p>
 * RateLimiter 限流 Client
 */
public interface RateLimiterClient {

	/**
	 * 服务是否被限流
	 * @param key 自定义的key，请保证唯一
	 * @param max 支持的最大请求
	 * @param ttl 时间,单位默认为秒（seconds）
	 * @return 是否允许
	 */
	default boolean isAllowed(String key, long max, long ttl) {
		return this.isAllowed(key, max, ttl, TimeUnit.SECONDS);
	}

	/**
	 * 服务是否被限流
	 * @param key 自定义的key，请保证唯一
	 * @param max 支持的最大请求
	 * @param ttl 时间
	 * @param timeUnit 时间单位
	 * @return 是否允许
	 */
	boolean isAllowed(String key, long max, long ttl, TimeUnit timeUnit);

	/**
	 * 服务限流，被限制时抛出 RateLimiterException 异常，需要自行处理异常
	 * @param key 自定义的key，请保证唯一
	 * @param max 支持的最大请求
	 * @param ttl 时间
	 * @param supplier Supplier 函数式
	 * @param <T> 泛型
	 * @return 函数执行结果
	 */
	default <T> T allow(String key, long max, long ttl, Supplier<T> supplier) throws Throwable {
		return allow(key, max, ttl, TimeUnit.SECONDS, supplier);
	}

	/**
	 * 服务限流，被限制时抛出 RateLimiterException 异常，需要自行处理异常
	 * @param <T> 泛型
	 * @param key 自定义的key，请保证唯一
	 * @param max 支持的最大请求
	 * @param ttl 时间
	 * @param timeUnit 时间单位
	 * @param supplier Supplier 函数式
	 * @return 函数执行结果
	 */
	default <T> T allow(String key, long max, long ttl, TimeUnit timeUnit, Supplier<T> supplier) throws Throwable {
		boolean isAllowed = this.isAllowed(key, max, ttl, timeUnit);
		if (isAllowed) {
			return supplier.get();
		}
		throw new RateLimiterException(key, max, ttl, timeUnit);
	}

}
