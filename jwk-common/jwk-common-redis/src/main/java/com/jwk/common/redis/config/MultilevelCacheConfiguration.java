package com.jwk.common.redis.config;

import com.jwk.common.redis.properties.CacheConfigProperties;
import com.jwk.common.redis.properties.RedisConfigProperties;
import com.jwk.common.redis.support.CacheMessageListener;
import com.jwk.common.redis.support.RedisCaffeineCacheManager;
import com.jwk.common.redis.support.RedisCaffeineCacheManagerCustomizer;
import com.jwk.common.redis.utils.RedisUtil;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.1
 * <p>
 * 多级缓存配置类
 */
@Slf4j
public class MultilevelCacheConfiguration {

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public RedissonClient redissonClient(RedisConfigProperties redisConfigProperties) {
		Config config = RedisUtil.config(redisConfigProperties);
		return Redisson.create(config);
	}


	@Bean
	@ConditionalOnMissingBean
	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
		return new RedissonConnectionFactory(redissonClient);
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisTemplate<Object, Object> stringKeyRedisTemplate(RedissonConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(RedisUtil.keySerializer());
		template.setHashKeySerializer(RedisUtil.keySerializer());
		template.setValueSerializer(RedisUtil.valueSerializer());
		template.setHashValueSerializer(RedisUtil.valueSerializer());
		template.setEnableDefaultSerializer(false);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(RedisTemplate.class)
	public RedisCaffeineCacheManager cacheManager(CacheConfigProperties cacheConfigProperties,
			RedisTemplate<Object, Object> redisTemplate,
			ObjectProvider<RedisCaffeineCacheManagerCustomizer> cacheManagerCustomizers) {
		RedisCaffeineCacheManager cacheManager = new RedisCaffeineCacheManager(cacheConfigProperties, redisTemplate);
		cacheManagerCustomizers.orderedStream().forEach(customizer -> customizer.customize(cacheManager));
		return cacheManager;
	}

	@Bean
	@SuppressWarnings("unchecked")
	@ConditionalOnMissingBean(name = "cacheMessageListener")
	public CacheMessageListener cacheMessageListener(RedisTemplate<Object, Object> redisTemplate,
			RedisCaffeineCacheManager redisCaffeineCacheManager) {
		return new CacheMessageListener((RedisSerializer<Object>) redisTemplate.getValueSerializer(),
				redisCaffeineCacheManager);
	}

	@Bean
	@ConditionalOnMissingBean(name = "cacheMessageListenerContainer")
	public RedisMessageListenerContainer cacheMessageListenerContainer(CacheConfigProperties cacheConfigProperties,
			@Qualifier("stringKeyRedisTemplate") RedisTemplate<Object, Object> stringKeyRedisTemplate,
			@Qualifier("cacheMessageListener") CacheMessageListener cacheMessageListener) {
		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer
				.setConnectionFactory(Objects.requireNonNull(stringKeyRedisTemplate.getConnectionFactory()));
		redisMessageListenerContainer.addMessageListener(cacheMessageListener,
				new ChannelTopic(cacheConfigProperties.getRedis().getTopic()));
		return redisMessageListenerContainer;
	}

}
