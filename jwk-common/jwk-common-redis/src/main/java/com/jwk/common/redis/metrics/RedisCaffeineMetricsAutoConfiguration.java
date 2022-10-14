package com.jwk.common.redis.metrics;

import io.lettuce.core.RedisClient;
import io.lettuce.core.metrics.MicrometerCommandLatencyRecorder;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * RedisCaffeine Metrics自动配置类
 * @date 2022/10/14
 */
@AutoConfiguration(before = RedisAutoConfiguration.class,
    after = { MetricsAutoConfiguration.class, CompositeMeterRegistryAutoConfiguration.class })
@ConditionalOnClass({ RedisClient.class, MicrometerCommandLatencyRecorder.class })
@ConditionalOnBean(MeterRegistry.class)
public class RedisCaffeineMetricsAutoConfiguration {

  @Bean
  public RedisCaffeineCacheMeterBinderProvider redisCaffeineCacheMeterBinderProvider() {
    return new RedisCaffeineCacheMeterBinderProvider();
  }


}
