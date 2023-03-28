package com.jwk.common.redis.factory.impl;

import cn.hutool.core.util.StrUtil;
import com.jwk.common.redis.enums.CacheType;
import com.jwk.common.redis.factory.CacheFactory;
import com.jwk.common.redis.properties.CacheConfigProperties;
import com.jwk.common.redis.support.RedisCaffeineCache;
import com.jwk.common.redis.utils.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 默认缓存创建类型
 * @date 2022/11/8
 */
public class DefaultCacheFactory implements CacheFactory {

	private final CacheConfigProperties cacheConfigProperties;

	private final RedisTemplate<String, Object> stringKeyRedisTemplate;

	public DefaultCacheFactory(CacheConfigProperties cacheConfigProperties,
			RedisTemplate<String, Object> stringKeyRedisTemplate) {
		this.cacheConfigProperties = cacheConfigProperties;
		this.stringKeyRedisTemplate = stringKeyRedisTemplate;
	}

	/**
	 * 创建缓存
	 * @param name
	 * @return
	 */
	@Override
	public RedisCaffeineCache createCache(String name) {
		boolean usedCaffeineCache = false;
		String[] redisConfigs = RedisUtil.replaceName(name, cacheConfigProperties.getRedis().getDelimiter());
		if (redisConfigs.length > 2) {
			String cacheType = redisConfigs[2];
			if (!StrUtil.isBlank(cacheType) && CacheType.RedisCaffeineCache.getCacheType().equals(cacheType)) {
				usedCaffeineCache = true;
			}
		}
		return new RedisCaffeineCache(name, stringKeyRedisTemplate, cacheConfigProperties, usedCaffeineCache);
	}

}
