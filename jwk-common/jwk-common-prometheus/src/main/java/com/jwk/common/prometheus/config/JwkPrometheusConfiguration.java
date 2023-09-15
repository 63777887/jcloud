package com.jwk.common.prometheus.config;

import com.jwk.common.prometheus.service.RegistryService;
import com.jwk.common.prometheus.service.impl.ZookeeperRegistryServiceImpl;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * Prometheus配置项
 * @date 2022/6/11
 */
public class JwkPrometheusConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(JwkPrometheusConfiguration.class);

	@Bean
	@ConditionalOnProperty(value = "jwk.prometheus.registryMode", havingValue = "zookeeper", matchIfMissing = true)
	public RegistryService zookeeperRegistryService(CuratorFramework curatorFramework,
			ApplicationContext applicationContext) {
		return new ZookeeperRegistryServiceImpl(curatorFramework, applicationContext);
	}

}
