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

package com.jwk.common.config;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.jwk.common.fegin.ext.SentinelFeign;
import com.jwk.common.fegin.handle.UrlBlockHandler;
import com.jwk.common.fegin.parser.HeaderRequestOriginParser;
import feign.Feign;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * sentinel 配置
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class FeignAutoConfiguration {

  @Bean
  @Scope("prototype")
  @ConditionalOnMissingBean
  @ConditionalOnProperty(name = "feign.sentinel.enabled")
  public Feign.Builder feignSentinelBuilder() {
    return SentinelFeign.builder();
  }

  @Bean
  @ConditionalOnMissingBean
  public BlockExceptionHandler blockExceptionHandler() {
    return new UrlBlockHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public RequestOriginParser requestOriginParser() {
    return new HeaderRequestOriginParser();
  }

}
