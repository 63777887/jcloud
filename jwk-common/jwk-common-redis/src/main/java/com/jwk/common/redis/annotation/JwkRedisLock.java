package com.jwk.common.redis.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 分布式锁注解
 * @date 2023/11/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface JwkRedisLock {

	/**
	 * key的前缀,默认取方法全限定名，除非我们在不同方法上对同一个资源做分布式锁，就自己指定
	 *
	 * @return key的前缀
	 */
	String prefixKey() default "";

	/**
	 * 分布式锁的 key， 支持springEl 表达式
	 *
	 * @return key
	 */
	@AliasFor("key")
	String value() default "";

	/**
	 * 分布式锁的 key， 支持springEl 表达式
	 *
	 * @return key
	 */
	@AliasFor("value")
	String key() default "";

	/**
	 * 等待锁的时间，默认-1，不等待直接失败,redisson默认也是-1
	 *
	 * @return waitTime
	 */
	int waitTime() default -1;

	/**
	 * 释放锁的时间，默认-1,redisson默认也是-1
	 * 注意：设置该值会导致redisson锁自动续期失效（看门狗）
	 * 详情请查看 {@link org.redisson.RedissonLock}
	 *
	 * @return leaseTime
	 */
	int leaseTime() default -1;

	/**
	 * 等待锁的时间单位，默认毫秒
	 *
	 * @return 单位
	 */
	TimeUnit unit() default TimeUnit.MILLISECONDS;

}
