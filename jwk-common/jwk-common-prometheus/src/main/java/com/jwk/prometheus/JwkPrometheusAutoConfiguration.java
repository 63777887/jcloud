package com.jwk.prometheus;


import com.jwk.common.cloud.config.FeignAutoConfiguration;
import com.jwk.prometheus.component.JwkPrometheusProperties;
import com.jwk.prometheus.config.JwkPrometheusConfiguration;
import com.jwk.prometheus.config.JwkPrometheusFactory;
import com.jwk.prometheus.ext.PrometheusSentinelFeign;
import feign.Feign;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 自动配置类
 */
@EnableConfigurationProperties(JwkPrometheusProperties.class)
@Import({JwkPrometheusConfiguration.class})
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class JwkPrometheusAutoConfiguration {

  private static Logger logger = LoggerFactory.getLogger(JwkPrometheusAutoConfiguration.class);


  public JwkPrometheusAutoConfiguration(ApplicationContext applicationContext,
      MeterRegistry registry, JwkPrometheusProperties jwkPrometheusProperties) {
    JwkPrometheusFactory.init(applicationContext,registry, jwkPrometheusProperties);
  }

  @Bean
  @Scope("prototype")
  @ConditionalOnProperty(name = "feign.sentinel.enabled")
  public Feign.Builder feignSentinelBuilder() {
    return PrometheusSentinelFeign.builder();
  }

}
