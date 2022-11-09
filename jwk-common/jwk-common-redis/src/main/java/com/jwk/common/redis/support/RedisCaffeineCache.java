package com.jwk.common.redis.support;

import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jwk.common.redis.properties.CacheConfigProperties;
import com.jwk.common.redis.properties.CaffeineCacheConfigProp;
import com.jwk.common.redis.utils.RedisUtil;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

/**
 * @author Jiwk
 * @date 2022/10/11
 * @version 0.1.3
 * <p>
 * {@link AbstractValueAdaptingCache} 缓存的数据操作
 */
@Slf4j
@Getter
public class RedisCaffeineCache extends AbstractValueAdaptingCache {

	private final String name;

	private final Cache<Object, Object> caffeineCache;

	private final RedisTemplate<String, Object> stringKeyRedisTemplate;

	private final String cachePrefix;

	private final Duration defaultExpiration;

	private final Map<String, Duration> expires;

	private final String topic;

	private final String delimiter;

	private final Boolean usedCaffeineCache;

	private final CaffeineCacheConfigProp caffeineCacheConfigProp;

	private final Object cacheServerId;

	private final Map<String, ReentrantLock> keyLockMap = new ConcurrentHashMap<>();

	public RedisCaffeineCache(String name, RedisTemplate<String, Object> stringKeyRedisTemplate, CacheConfigProperties cacheConfigProperties,Boolean usedCaffeineCache) {
		super(cacheConfigProperties.isCacheNullValues());
		this.name = name;
		this.stringKeyRedisTemplate = stringKeyRedisTemplate;
		this.cachePrefix = cacheConfigProperties.getCachePrefix();
		this.defaultExpiration = cacheConfigProperties.getRedis().getDefaultExpiration();
		this.expires = cacheConfigProperties.getRedis().getExpires();
		this.topic = cacheConfigProperties.getRedis().getTopic();
		this.delimiter = cacheConfigProperties.getRedis().getDelimiter();
		this.caffeineCacheConfigProp = cacheConfigProperties.getCaffeine();
		this.caffeineCache = caffeineCache();
		this.usedCaffeineCache = usedCaffeineCache;
		this.cacheServerId = cacheConfigProperties.getServerId();
	}

	@Override
	public Object getNativeCache() {
		return this;
	}

	/**
	 * 获取缓存
	 * @param key
	 * @param valueLoader
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		Object value = lookup(key);
		if (value != null) {
			return (T) value;
		}

		ReentrantLock lock = keyLockMap.computeIfAbsent(key.toString(), s -> {
			if (log.isDebugEnabled()) {
				log.debug("create lock for key : {}", s);
			}
			return new ReentrantLock();
		});

		lock.lock();
		try {
			value = lookup(key);
			if (value != null) {
				return (T) value;
			}
			value = valueLoader.call();
			Object storeValue = toStoreValue(value);
			put(key, storeValue);
			return (T) value;
		}
		catch (Exception e) {
			throw new ValueRetrievalException(key, valueLoader, e.getCause());
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * 添加缓存
	 * @param key
	 * @param value
	 */
	@Override
	public void put(Object key, Object value) {
		if (!super.isAllowNullValues() && value == null) {
			this.evict(key);
			return;
		}
		doPut(key, value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		Object prevValue;
		// 考虑使用分布式锁，或者将redis的setIfAbsent改为原子性操作
		synchronized (key) {
			prevValue = getRedisValue(key);
			if (prevValue == null) {
				doPut(key, value);
			}
		}
		return toValueWrapper(prevValue);
	}

	private void doPut(Object key, Object value) {
		value = toStoreValue(value);
		Duration expire = getExpire();
		setRedisValue(key, value, expire);

		if (this.usedCaffeineCache) {
			push(key);

			setCaffeineValue(key, value);
		}
	}

	/**
	 * 清除缓存
	 * @param key
	 */
	@Override
	public void evict(Object key) {
		// 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
		stringKeyRedisTemplate.delete(getKey(key));

		if (this.usedCaffeineCache) {
			push(key);

			caffeineCache.invalidate(key);
		}
	}

	@Override
	public void clear() {
		// 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
		Set<String> keys = stringKeyRedisTemplate.keys(this.name.concat(":*"));

		if (!CollectionUtils.isEmpty(keys)) {
			stringKeyRedisTemplate.delete(keys);
		}

		if (this.usedCaffeineCache) {
			push(null);

			caffeineCache.invalidateAll();
		}
	}

	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	@Override
	protected Object lookup(Object key) {
		Object cacheKey = getKey(key);
		Object value;
		if (usedCaffeineCache) {
			value = getCaffeineValue(key);
			if (value != null) {
				if (log.isDebugEnabled()){
					log.debug("get cache from caffeine, the key is : {}", cacheKey);
				}
				return value;
			}
		}

		value = getRedisValue(key);

		if (value != null) {
			if (log.isDebugEnabled()) {
				log.debug("get cache from redis and put in caffeine, the key is : {}", cacheKey);
			}
			setCaffeineValue(key, value);
		}
		return value;
	}

	protected String getKey(Object key) {
		return this.name.concat(":").concat(
				StrUtil.isNotBlank(cachePrefix) ? cachePrefix.concat(":").concat(key.toString()) : key.toString());
	}

	protected Duration getExpire() {
		String[] cacheNameConfigs = RedisUtil.replaceName(this.name, this.delimiter);
		Duration cacheNameExpire =expires.get(cacheNameConfigs.length > 1 ? cacheNameConfigs[0] : this.name);
		if (cacheNameExpire == null && cacheNameConfigs.length > 1) {
			try {
				// 支持时间单位例如：60m，第二个参数是默认单位
				cacheNameExpire = DurationStyle.detectAndParse(cacheNameConfigs[1], ChronoUnit.SECONDS);
			}
			catch (NumberFormatException e) {
				if (log.isWarnEnabled()) {
					log.warn("cacheable has illegal expire time: {}, will use default settings", cacheNameConfigs[1]);
				}
			}
		}
		if (cacheNameExpire == null) {
			cacheNameExpire = defaultExpiration;
		}
		return cacheNameExpire;
	}

	/**
	 * 缓存变更时通知其他节点清理本地缓存
	 * @param key
	 */
	protected void push(Object key) {
		stringKeyRedisTemplate.convertAndSend(topic, new CacheMessage(this.name,key,this.cacheServerId));
	}

	/**
	 * 清理本地缓存
	 * @param key
	 */
	public void clearLocal(Object key) {
		if (log.isDebugEnabled()) {
			log.debug("clear local cache, the key is : {}", key);
		}
		if (key == null) {
			caffeineCache.invalidateAll();
		}
		else {
			caffeineCache.invalidate(key);
		}
	}

	protected void setRedisValue(Object key, Object value, Duration expire) {
		if (!expire.isNegative() && !expire.isZero()) {
			stringKeyRedisTemplate.opsForValue().set(getKey(key), value, expire);
		}
		else {
			stringKeyRedisTemplate.opsForValue().set(getKey(key), value);
		}
	}

	protected Object getRedisValue(Object key) {
		return stringKeyRedisTemplate.opsForValue().get(getKey(key));
	}

	protected boolean setRedisValueIfAbsent(Object key,Object value) {
		return stringKeyRedisTemplate.opsForValue().setIfAbsent(getKey(key),value);
	}

	protected void setCaffeineValue(Object key, Object value) {
		caffeineCache.put(key, value);
	}

	protected Object getCaffeineValue(Object key) {
		return caffeineCache.getIfPresent(key);
	}


	protected com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache() {
		return caffeineCacheBuilder().build();
	}

	protected Caffeine<Object, Object> caffeineCacheBuilder() {
		Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
		doIfPresent(getExpire(), cacheBuilder::expireAfterWrite);
		if (caffeineCacheConfigProp.getInitialCapacity() > 0) {
			cacheBuilder.initialCapacity(caffeineCacheConfigProp.getInitialCapacity());
		}
		if (caffeineCacheConfigProp.getMaximumSize() > 0) {
			cacheBuilder.maximumSize(caffeineCacheConfigProp.getMaximumSize());
		}
		if (caffeineCacheConfigProp.getKeyStrength() != null) {
			switch (caffeineCacheConfigProp.getKeyStrength()) {
				case WEAK:
					// Caffeine.weakKeys() 使用弱引用存储key。如果没有强引用这个key，则GC时允许回收该条目
					cacheBuilder.weakKeys();
					break;
				case SOFT:
					throw new UnsupportedOperationException("caffeine 不支持 key 软引用");
				default:
			}
		}
		if (caffeineCacheConfigProp.getValueStrength() != null) {
			switch (caffeineCacheConfigProp.getValueStrength()) {
				case WEAK:
					// Caffeine.weakValues() 使用弱引用存储value。如果没有强引用这个value，则GC时允许回收该条目
					cacheBuilder.weakValues();
					break;
				case SOFT:
					// Caffeine.softValues() 使用软引用存储value, 如果没有强引用这个value，则GC内存不足时允许回收该条目
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
}
