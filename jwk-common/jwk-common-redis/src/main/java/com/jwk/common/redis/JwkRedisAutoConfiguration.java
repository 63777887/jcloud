package com.jwk.common.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.jwk.common.redis.config.RateLimiterAutoConfiguration;
import com.jwk.common.redis.config.RedisKeyExpiredEventConfiguration;
import com.jwk.common.redis.factory.CacheFactory;
import com.jwk.common.redis.factory.CacheServerIdFactory;
import com.jwk.common.redis.properties.CacheConfigProperties;
import com.jwk.common.redis.properties.RedisConfigProperties;
import com.jwk.common.redis.support.CacheMessageListener;
import com.jwk.common.redis.support.RedisCaffeineCacheManager;
import com.jwk.common.redis.support.RedisCaffeineCacheManagerCustomizer;
import com.jwk.common.redis.utils.RedisUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.3
 * <p>
 * 自动注入类
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({ RedisAutoConfiguration.class, })
@EnableConfigurationProperties({ CacheConfigProperties.class, RedisConfigProperties.class })
@Slf4j
@Import({ RateLimiterAutoConfiguration.class, RedisKeyExpiredEventConfiguration.class })
public class JwkRedisAutoConfiguration {

	@Bean
	@Primary
	public RedisProperties redisProperties(RedisProperties redisProperties,
			RedisConfigProperties redisConfigProperties) {
		CopyOptions copyOptions = new CopyOptions();
		copyOptions.ignoreNullValue();
		BeanUtil.copyProperties(redisConfigProperties.getRedis(), redisProperties);
		return redisProperties;
	}

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
	@ConditionalOnMissingBean(name = "stringKeyRedisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedissonConnectionFactory redissonConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redissonConnectionFactory);
		redisTemplate.setKeySerializer(RedisUtil.keySerializer());
		redisTemplate.setHashKeySerializer(RedisUtil.keySerializer());
		redisTemplate.setValueSerializer(RedisUtil.valueSerializer());
		redisTemplate.setHashValueSerializer(RedisUtil.valueSerializer());
		redisTemplate.setEnableDefaultSerializer(false);
		return redisTemplate;
	}

	@Bean
	@ConditionalOnMissingBean(name = "cacheMessageListenerContainer")
	public RedisMessageListenerContainer redisMessageListenerContainer(
			 RedisTemplate<String, Object> stringKeyRedisTemplate) {
		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer
				.setConnectionFactory(Objects.requireNonNull(stringKeyRedisTemplate.getConnectionFactory()));
		return redisMessageListenerContainer;
	}

	@Bean
	@ConditionalOnMissingBean
	public KeyExpirationEventMessageListener keyExpirationEventMessageListener(
			RedisMessageListenerContainer listenerContainer) {
		return new KeyExpirationEventMessageListener(listenerContainer);
	}

}
