package com.jwk.common.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwk.common.redis.config.RateLimiterAutoConfiguration;
import com.jwk.common.redis.config.RedisKeyExpiredEventConfiguration;
import com.jwk.common.redis.lock.RedisLockAspect;
import com.jwk.common.redis.lock.RedisLockService;
import com.jwk.common.redis.properties.CacheConfigProperties;
import com.jwk.common.redis.properties.RedisConfigProperties;
import com.jwk.common.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Objects;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 自动注入类
 * @date 2022/10/10
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({RedisAutoConfiguration.class,})
@EnableConfigurationProperties({CacheConfigProperties.class, RedisConfigProperties.class})
@Slf4j
@Import({RateLimiterAutoConfiguration.class, RedisKeyExpiredEventConfiguration.class, RedisLockService.class})
public class JwkRedisAutoConfiguration {

    /**
     * redis配置文件
     * @param redisProperties
     * @param redisConfigProperties
     * @return
     */
    @Bean
    @Primary
    public RedisProperties redisProperties(RedisProperties redisProperties,
                                           RedisConfigProperties redisConfigProperties) {
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(redisConfigProperties.getRedis(), redisProperties);
        return redisProperties;
    }

    /**
     * redisson客户端
     * @param redisProperties
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        return Redisson.create(RedisUtil.config(redisProperties));
    }

    /**
     * Redisson连接工厂
     * @param redissonClient
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    /**
     * Object RedisTemplate
     * @param redissonConnectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedissonConnectionFactory redissonConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(RedisUtil.keySerializer());
        redisTemplate.setHashKeySerializer(RedisUtil.keySerializer());
        redisTemplate.setValueSerializer(RedisUtil.valueSerializer());
        redisTemplate.setHashValueSerializer(RedisUtil.valueSerializer());
        redisTemplate.setConnectionFactory(redissonConnectionFactory);
        return redisTemplate;
    }

    /**
     * String RedisTemplate
     * @param redissonConnectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedissonConnectionFactory redissonConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setKeySerializer(RedisUtil.keySerializer());
        redisTemplate.setHashKeySerializer(RedisUtil.keySerializer());
        redisTemplate.setValueSerializer(RedisUtil.stringValueSerializer());
        redisTemplate.setHashValueSerializer(RedisUtil.stringValueSerializer());
        redisTemplate.setConnectionFactory(redissonConnectionFactory);
        return redisTemplate;
    }

    /**
     * String RedisTemplate操作
     * @param stringRedisTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ValueOperations.class)
    public ValueOperations<String, String> valueOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForValue();
    }

    /**
     * 监听容器
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "cacheMessageListenerContainer")
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisTemplate<String, Object> redisTemplate) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer
                .setConnectionFactory(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        return redisMessageListenerContainer;
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyExpirationEventMessageListener keyExpirationEventMessageListener(
            RedisMessageListenerContainer listenerContainer) {
        return new KeyExpirationEventMessageListener(listenerContainer);
    }

    @Bean
    public RedisLockAspect redisRateLimiterAspect(RedisLockService redisLockService) {
        return new RedisLockAspect(redisLockService);
    }

}
