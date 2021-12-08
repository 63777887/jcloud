package com.jwk.common.cloud.config;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.jwk.common.cloud.fegin.ext.SentinelFeign;
import com.jwk.common.cloud.fegin.handle.UrlBlockHandler;
import com.jwk.common.cloud.fegin.parser.HeaderRequestOriginParser;
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
