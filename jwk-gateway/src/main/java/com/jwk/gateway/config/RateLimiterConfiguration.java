package com.jwk.gateway.config;

import java.util.Objects;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 路由限流配置
 */
@Configuration(proxyBeanMethods = false)
public class RateLimiterConfiguration {

  /**
   * Remote addr key resolver key resolver.
   *
   * @link {https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory}
   */
  /*
  spring:
  cloud:
    gateway:
      routes:
      - id: requestratelimiter_route
        uri: lb://pigx-upms
        order: 10000
        predicates:
        - Path=/admin/**
        filters:
        - name: RequestRateLimiter
          args:
            redis-rate-limiter.replenishRate: 1  # 令牌桶的容积
            redis-rate-limiter.burstCapacity: 3  # 流速 每秒
            key-resolver: "#{@remoteAddrKeyResolver}" #SPEL表达式去的对应的bean
        - StripPrefix=1
   */
  @Bean
  public KeyResolver remoteAddrKeyResolver() {
    //jwk:获取访问者的ip地址, 通过访问者ip地址进行限流, 限流使用的是Redis中的令牌桶算法
    return exchange -> Mono
        .just(
            Objects.requireNonNull(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()))
                .getAddress().getHostAddress());
  }

}
