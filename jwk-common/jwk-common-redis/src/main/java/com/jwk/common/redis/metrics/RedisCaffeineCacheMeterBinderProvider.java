package com.jwk.common.redis.metrics;

import com.jwk.common.redis.support.RedisCaffeineCache;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import lombok.NoArgsConstructor;
import org.springframework.boot.actuate.metrics.cache.CacheMeterBinderProvider;


/**
 * @author Jiwk
 * @date 2022/10/14
 * @version 0.1.1
 * <p>
 * RedisCaffeineCache meter
 * {@link org.springframework.boot.actuate.metrics.cache.CaffeineCacheMeterBinderProvider}
 * {@link org.springframework.boot.actuate.metrics.cache.RedisCacheMeterBinderProvider}
 */
@NoArgsConstructor
public class RedisCaffeineCacheMeterBinderProvider implements CacheMeterBinderProvider<RedisCaffeineCache> {

	@Override
	public MeterBinder getMeterBinder(RedisCaffeineCache cache, Iterable<Tag> tags) {
		return new CaffeineCacheMetrics(cache.getCaffeineCache(), cache.getName(), tags);
	}

}
