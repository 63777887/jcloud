package com.jwk.common.redis.support;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.jwk.common.redis.properties.CacheConfigProperties;
import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Jiwk
 * @date 2022/10/11
 * @version 0.1.1
 * <p>
 * {@link CacheManager} 实现自定义缓存
 */
@Slf4j
@Getter
@Setter
public class RedisCaffeineCacheManager implements CacheManager {

	private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

	private CacheConfigProperties cacheConfigProperties;

	private RedisTemplate<Object, Object> stringKeyRedisTemplate;

	private boolean dynamic;

	private Set<String> cacheNames;

	public RedisCaffeineCacheManager(CacheConfigProperties cacheConfigProperties,
			RedisTemplate<Object, Object> stringKeyRedisTemplate) {
		super();
		this.cacheConfigProperties = cacheConfigProperties;
		this.stringKeyRedisTemplate = stringKeyRedisTemplate;
		this.dynamic = cacheConfigProperties.isDynamic();
		this.cacheNames = cacheConfigProperties.getCacheNames();
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

		cache = createCache(name);
		Cache oldCache = cacheMap.putIfAbsent(name, cache);
		log.debug("create cache instance, the cache name is : {}", name);
		return oldCache == null ? cache : oldCache;
	}

	public RedisCaffeineCache createCache(String name) {
		return new RedisCaffeineCache(name, stringKeyRedisTemplate, caffeineCache(), cacheConfigProperties);
	}

	public com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache() {
		return caffeineCacheBuilder().build();
	}

	public Caffeine<Object, Object> caffeineCacheBuilder() {
		Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
		doIfPresent(cacheConfigProperties.getCaffeine().getExpireAfterAccess(), cacheBuilder::expireAfterAccess);
		doIfPresent(cacheConfigProperties.getCaffeine().getExpireAfterWrite(), cacheBuilder::expireAfterWrite);
		doIfPresent(cacheConfigProperties.getCaffeine().getRefreshAfterWrite(), cacheBuilder::refreshAfterWrite);
		if (cacheConfigProperties.getCaffeine().getInitialCapacity() > 0) {
			cacheBuilder.initialCapacity(cacheConfigProperties.getCaffeine().getInitialCapacity());
		}
		if (cacheConfigProperties.getCaffeine().getMaximumSize() > 0) {
			cacheBuilder.maximumSize(cacheConfigProperties.getCaffeine().getMaximumSize());
		}
		if (cacheConfigProperties.getCaffeine().getKeyStrength() != null) {
			switch (cacheConfigProperties.getCaffeine().getKeyStrength()) {
			case WEAK:
				//Caffeine.weakKeys() 使用弱引用存储key。如果没有强引用这个key，则GC时允许回收该条目
				cacheBuilder.weakKeys();
				break;
			case SOFT:
				throw new UnsupportedOperationException("caffeine 不支持 key 软引用");
			default:
			}
		}
		if (cacheConfigProperties.getCaffeine().getValueStrength() != null) {
			switch (cacheConfigProperties.getCaffeine().getValueStrength()) {
			case WEAK:
				//Caffeine.weakValues() 使用弱引用存储value。如果没有强引用这个value，则GC时允许回收该条目
				cacheBuilder.weakValues();
				break;
			case SOFT:
				//Caffeine.softValues() 使用软引用存储value, 如果没有强引用这个value，则GC内存不足时允许回收该条目
				cacheBuilder.softValues();
				break;
			default:
			}
		}
		return cacheBuilder;
	}

	protected static void doIfPresent(Duration duration, Consumer<Duration> consumer) {
		if (duration != null && !duration.isNegative()) {
			consumer.accept(duration);
		}
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
