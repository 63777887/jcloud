package com.jwk.common.redis.config;

import com.jwk.common.redis.factory.CacheFactory;
import com.jwk.common.redis.factory.CacheServerIdFactory;
import com.jwk.common.redis.factory.impl.DefaultCacheFactory;
import com.jwk.common.redis.factory.impl.RedisSeqCacheServerIdFactory;
import com.jwk.common.redis.properties.CacheConfigProperties;
import com.jwk.common.redis.support.CacheMessageListener;
import com.jwk.common.redis.support.RedisCaffeineCacheManager;
import com.jwk.common.redis.support.RedisCaffeineCacheManagerCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Objects;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 多级缓存配置类
 * @date 2022/10/10
 */
@Slf4j
public class MultilevelCacheConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RedisCaffeineCacheManager cacheManager(CacheConfigProperties cacheConfigProperties,
												   RedisTemplate<String, Object> redisTemplate,
			ObjectProvider<RedisCaffeineCacheManagerCustomizer> cacheManagerCustomizers,
			ObjectProvider<CacheServerIdFactory> cacheServerIdFactories, CacheFactory cacheFactory) {
		Object cacheServerId = cacheConfigProperties.getServerId();
		if (cacheServerId == null || "".equals(cacheServerId)) {
			cacheServerIdFactories
					.ifAvailable(serverIdGenerator -> cacheConfigProperties.setServerId(serverIdGenerator.get()));
		}
		RedisCaffeineCacheManager cacheManager = new RedisCaffeineCacheManager(cacheConfigProperties, redisTemplate,
				cacheFactory);
		cacheManagerCustomizers.orderedStream().forEach(customizer -> customizer.customize(cacheManager));
		return cacheManager;
	}

	@Bean
	@SuppressWarnings("unchecked")
	@ConditionalOnMissingBean(name = "cacheMessageListener")
	public CacheMessageListener cacheMessageListener(RedisTemplate<String, Object> redisTemplate,
			RedisCaffeineCacheManager redisCaffeineCacheManager) {
		return new CacheMessageListener((RedisSerializer<Object>) redisTemplate.getValueSerializer(),
				redisCaffeineCacheManager);
	}

	@Bean
	@ConditionalOnMissingBean(name = "cacheMessageListenerContainer")
	public RedisMessageListenerContainer cacheMessageListenerContainer(CacheConfigProperties cacheConfigProperties,
			RedisTemplate<String, Object> redisTemplate,
			@Qualifier("cacheMessageListener") CacheMessageListener cacheMessageListener) {
		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer
				.setConnectionFactory(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
		redisMessageListenerContainer.addMessageListener(cacheMessageListener,
				new ChannelTopic(cacheConfigProperties.getRedis().getTopic()));
		return redisMessageListenerContainer;
	}

	@Bean
	@ConditionalOnMissingBean(CacheFactory.class)
	public CacheFactory cacheFactory(CacheConfigProperties cacheConfigProperties,
			RedisTemplate<String, Object> redisTemplate) {
		return new DefaultCacheFactory(cacheConfigProperties, redisTemplate);
	}

	@Bean
	@ConditionalOnMissingBean(CacheServerIdFactory.class)
	public CacheServerIdFactory cacheServerIdFactory(CacheConfigProperties cacheConfigProperties,
			RedisTemplate<String, Object> redisTemplate) {
		return new RedisSeqCacheServerIdFactory(cacheConfigProperties, redisTemplate);
	}

}
