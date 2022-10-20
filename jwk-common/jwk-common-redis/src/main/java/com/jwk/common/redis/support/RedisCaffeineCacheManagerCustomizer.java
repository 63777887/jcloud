package com.jwk.common.redis.support;

/**
 * @author Jiwk
 * @date 2022/10/14
 * @version 0.1.1
 * <p>
 * 修改 {@link RedisCaffeineCacheManager} 的回调
 */
@FunctionalInterface
public interface RedisCaffeineCacheManagerCustomizer {

	/**
	 * 修改 {@link RedisCaffeineCacheManager}
	 * @param cacheManager cacheManager
	 */
	void customize(RedisCaffeineCacheManager cacheManager);

}
