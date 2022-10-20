package com.jwk.common.redis.config;

import com.jwk.common.redis.properties.CacheConfigProperties;
import com.jwk.common.redis.support.CacheMessageListener;
import com.jwk.common.redis.support.RedisCaffeineCacheManager;
import com.jwk.common.redis.support.RedisCaffeineCacheManagerCustomizer;
import java.util.Objects;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.1
 * <p>
 * 多级缓存配置类
 */
public class MultilevelCacheConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = "stringKeyRedisTemplate")
	public RedisTemplate<Object, Object> stringKeyRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(keySerializer());
		template.setHashKeySerializer(keySerializer());
		template.setValueSerializer(valueSerializer());
		template.setHashValueSerializer(valueSerializer());
		template.setEnableDefaultSerializer(false);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(RedisTemplate.class)
	public RedisCaffeineCacheManager cacheManager(CacheConfigProperties cacheConfigProperties,
			@Qualifier("stringKeyRedisTemplate") RedisTemplate<Object, Object> stringKeyRedisTemplate,
			ObjectProvider<RedisCaffeineCacheManagerCustomizer> cacheManagerCustomizers) {
		RedisCaffeineCacheManager cacheManager = new RedisCaffeineCacheManager(cacheConfigProperties,
				stringKeyRedisTemplate);
		cacheManagerCustomizers.orderedStream().forEach(customizer -> customizer.customize(cacheManager));
		return cacheManager;
	}

	@Bean
	@SuppressWarnings("unchecked")
	@ConditionalOnMissingBean(name = "cacheMessageListener")
	public CacheMessageListener cacheMessageListener(
			@Qualifier("stringKeyRedisTemplate") RedisTemplate<Object, Object> stringKeyRedisTemplate,
			RedisCaffeineCacheManager redisCaffeineCacheManager) {
		return new CacheMessageListener((RedisSerializer<Object>) stringKeyRedisTemplate.getValueSerializer(),
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

	/**
	 * redis键序列化使用StringRedisSerializer
	 */
	private RedisSerializer<String> keySerializer() {
		return new StringRedisSerializer();
	}

	/**
	 * redis值序列化使用json序列化器
	 */
	private RedisSerializer<Object> valueSerializer() {
		return new GenericJackson2JsonRedisSerializer();
	}

}
