package com.jwk.common.redis;

import com.jwk.common.redis.config.MultilevelCacheConfiguration;
import com.jwk.common.redis.properties.CacheConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.1
 * <p>
 * 自动注入类
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheConfigProperties.class)
@Import(MultilevelCacheConfiguration.class)
public class JwkRedisAutoConfiguration {

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

	/**
	 *  redis键序列化使用StringRedisSerializer
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
