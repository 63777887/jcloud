package com.jwk.redisLock.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {


  @Bean
  public Redisson redisson(){
      // 1. Create config object
    Config config = new Config();
    config.useSingleServer()
            // use "rediss://" for SSL connection
            .setAddress("redis://127.0.0.1:6379").setDatabase(0);
    return (Redisson)Redisson.create(config);
  }
  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);

    RedisSerializer<Object> serializer = redisSerializer();
    // 指定key和value的序列化和反序列化的方式
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(serializer);

    redisTemplate.afterPropertiesSet();

    return redisTemplate;
  }

  @Bean
  public RedisSerializer<Object> redisSerializer() {
    Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(
        Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
    serializer.setObjectMapper(objectMapper);
    return serializer;
  }

  @Bean
  public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheWriter redisCacheWriter = RedisCacheWriter
        .nonLockingRedisCacheWriter(redisConnectionFactory);

    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer())).entryTtl(
            Duration.ofDays(1));
    return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);

  }
}
