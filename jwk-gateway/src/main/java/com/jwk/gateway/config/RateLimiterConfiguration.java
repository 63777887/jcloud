/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jwk.gateway.config;

import java.util.Objects;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author lengleng
 * @date 2019/2/1 路由限流配置
 */
@Configuration(proxyBeanMethods = false)
public class RateLimiterConfiguration {

  /**
   * Remote addr key resolver key resolver.
   *
   * @link {https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory}
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
