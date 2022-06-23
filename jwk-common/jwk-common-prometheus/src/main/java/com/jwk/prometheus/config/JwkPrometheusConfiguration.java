package com.jwk.prometheus.config;


import com.jwk.prometheus.component.JwkPrometheusProperties;
import com.jwk.prometheus.component.ZookeeperProperties;
import com.jwk.prometheus.service.RegistryService;
import com.jwk.prometheus.service.impl.ZookeeperRegistryServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Prometheus配置项
 */
public class JwkPrometheusConfiguration {

  private static Logger logger = LoggerFactory.getLogger(JwkPrometheusConfiguration.class);


  @Bean(initMethod = "start")
  @ConditionalOnMissingBean
  @ConditionalOnExpression(
      "#{'zookeeper'.equals(environment['jwk.prometheus.registryMode']) "
          + "or ''.equals(environment['jwk.prometheus.registryMode'])"
          + "or null==(environment['jwk.prometheus.registryMode'])}"
  )
  public CuratorFramework curatorFramework(JwkPrometheusProperties jwkPrometheusProperties) {
    ZookeeperProperties zookeeperInfo = jwkPrometheusProperties.getZookeeper();
    if (StringUtils.isBlank(zookeeperInfo.getAddress())) {
      throw new RuntimeException("jcloud prometheus address is undefined, please check config");
    }
    if (StringUtils.isBlank(zookeeperInfo.getNamespace())) {
      throw new RuntimeException("jcloud prometheus namespace is undefined, please check config");
    }
    CuratorFramework client = CuratorFrameworkFactory
        .builder().connectString(zookeeperInfo.getAddress()).canBeReadOnly(true)
        .connectionTimeoutMs(zookeeperInfo.getConnectionTimeout())
        .sessionTimeoutMs(zookeeperInfo.getSessionTimeout())
        .retryPolicy(new ExponentialBackoffRetry(zookeeperInfo.getRetryIntervalMs(), zookeeperInfo.getRetryNumber()))
        .build();
    return client;
  }

  @Bean
  @ConditionalOnExpression(
      "#{'zookeeper'.equals(environment['jwk.prometheus.registryMode']) "
          + "or ''.equals(environment['jwk.prometheus.registryMode'])"
          + "or null==(environment['jwk.prometheus.registryMode'])}"
  )
  public RegistryService zookeeperRegistryService() {
    return new ZookeeperRegistryServiceImpl();
  }

  @Bean
  public JwkPrometheusContext jwkPrometheusContext() {
    JwkPrometheusContext jwkPrometheusContext = JwkPrometheusFactory.getContext();
    return jwkPrometheusContext;
  }

}
