package com.jwk.common.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.jwk.common.redis.properties.CacheConfigProperties;
import com.jwk.common.redis.properties.RedisConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.1
 * <p>
 * 自动注入类
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({RedisAutoConfiguration.class,})
@EnableConfigurationProperties({CacheConfigProperties.class, RedisConfigProperties.class})
public class JwkRedisAutoConfiguration {

  @Bean
  @Primary
  public RedisProperties redisProperties(RedisProperties redisProperties,RedisConfigProperties redisConfigProperties){
    CopyOptions copyOptions = new CopyOptions();
    copyOptions.ignoreNullValue();
    BeanUtil.copyProperties(redisConfigProperties.getRedis(),redisProperties);
    return redisProperties;
  }

}
