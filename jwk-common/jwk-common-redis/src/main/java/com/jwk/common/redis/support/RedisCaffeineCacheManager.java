package com.jwk.common.redis.support;

import com.jwk.common.redis.factory.CacheFactory;
import com.jwk.common.redis.properties.CacheConfigProperties;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Jiwk
 * @date 2022/10/11
 * @version 0.1.3
 * <p>
 * {@link CacheManager} 实现自定义缓存
 */
@Slf4j
@Getter
@Setter
public class RedisCaffeineCacheManager implements CacheManager {

	private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

	private CacheConfigProperties cacheConfigProperties;

	private RedisTemplate<String, Object> stringKeyRedisTemplate;

	private boolean dynamic;

	private Set<String> cacheNames;

	private CacheFactory cacheFactory;

	private Object cacheSeverId;

	public RedisCaffeineCacheManager(CacheConfigProperties cacheConfigProperties,
			RedisTemplate<String, Object> stringKeyRedisTemplate,
			CacheFactory cacheFactory) {
		this.cacheConfigProperties = cacheConfigProperties;
		this.stringKeyRedisTemplate = stringKeyRedisTemplate;
		this.dynamic = cacheConfigProperties.isDynamic();
		this.cacheNames = cacheConfigProperties.getCacheNames();
		this.cacheFactory = cacheFactory;
		this.cacheSeverId = cacheConfigProperties.getServerId();
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = cacheMap.get(name);
		if (cache != null) {
			return cache;
		}
		if (!dynamic && !cacheNames.contains(name)) {
			return null;
		}

		cache = cacheFactory.createCache(name);
		Cache oldCache = cacheMap.putIfAbsent(name, cache);
		if (log.isDebugEnabled()) {
			log.debug("create cache instance, the cache name is : {}", name);
		}
		return oldCache == null ? cache : oldCache;
	}

	@Override
	public Collection<String> getCacheNames() {
		return this.cacheNames;
	}

	public void clearLocal(String cacheName, Object key) {
		Cache cache = cacheMap.get(cacheName);
		if (cache == null) {
			return;
		}

		RedisCaffeineCache redisCaffeineCache = (RedisCaffeineCache) cache;
		redisCaffeineCache.clearLocal(key);
	}

}
