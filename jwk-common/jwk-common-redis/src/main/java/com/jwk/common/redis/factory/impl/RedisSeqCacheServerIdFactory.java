package com.jwk.common.redis.factory.impl;

import com.jwk.common.redis.factory.CacheServerIdFactory;
import com.jwk.common.redis.properties.CacheConfigProperties;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 通过redis key生成serverId
 * @date 2022/11/9
 */
public class RedisSeqCacheServerIdFactory implements CacheServerIdFactory {

	protected final RedisTemplate<String, Object> redisTemplate;

	protected final CacheConfigProperties properties;

	public RedisSeqCacheServerIdFactory(CacheConfigProperties properties,
			RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.properties = properties;
	}

	@Override
	public Object get() {
		return redisTemplate.opsForValue().increment(properties.getRedis().getServerIdGeneratorKey());
	}

}
