package com.jwk.common.redis.factory;

import com.jwk.common.redis.support.RedisCaffeineCache;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 指定创建缓存的类型
 * @date 2022/11/8
 */
public interface CacheFactory {

	/**
	 * 创建缓存
	 * @param name
	 * @return
	 */
	RedisCaffeineCache createCache(String name);

}
