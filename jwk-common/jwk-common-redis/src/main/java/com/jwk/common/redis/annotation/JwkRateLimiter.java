package com.jwk.common.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author Jiwk
 * @date 2022/11/10
 * @version 0.1.3
 * <p>
 * 分布式 限流注解，默认速率为 600/ms
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface JwkRateLimiter {

	/**
	 * 限流的 key 支持，必须：请保持唯一性
	 * @return key
	 */
	String value();

	/**
	 * 限流的参数，可选，支持 spring el # 读取方法参数和 @ 读取 spring bean
	 * @return param
	 */
	String param() default "";

	/**
	 * 支持的最大请求，默认: 100
	 * @return 请求数
	 */
	long max() default 100L;

	/**
	 * 持续时间，默认: 3600
	 * @return 持续时间
	 */
	long ttl() default 3600L;

	/**
	 * 时间单位，默认为分
	 * @return TimeUnit
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

}
