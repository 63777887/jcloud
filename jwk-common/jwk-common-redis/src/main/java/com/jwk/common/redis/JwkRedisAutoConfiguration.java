package com.jwk.common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwk.common.redis.properties.CacheConfigProperties;
import com.jwk.common.redis.properties.RedisConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.1
 * <p>
 * 自动注入类
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({CacheConfigProperties.class, RedisConfigProperties.class})
public class JwkRedisAutoConfiguration {
  @Bean
  public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    // 非阻塞比阻塞（基于锁）并发要快得多
    RedisCacheWriter redisCacheWriter = RedisCacheWriter
        .nonLockingRedisCacheWriter(redisConnectionFactory);
    //设置Redis缓存有效期为1天
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()));
    return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
  }

  @Bean
  public RedisSerializer<Object> redisSerializer() {
    //创建JSON序列化器
    // 使用GenericJacksonRedisSerializer比Jackson2JsonRedisSerializer效率低，占用内存高
    Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(
        Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
    // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等抛异常
    // redis会在key中保存类的全限定名，这样可以在反序列化的时候生成对应的类
    // "[\"club.banyuan.demo.redis.user.User\",{\"name\":\"\xe5\x8d\x8a\xe5\x9c\x86\",\"pwd\":\"123456\"}]"
    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    serializer.setObjectMapper(objectMapper);
    return serializer;
  }

}
