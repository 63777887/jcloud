package com.jwk.common.prometheus.config;

import com.jwk.common.prometheus.properties.JwkPrometheusProperties;
import com.jwk.common.prometheus.properties.ZookeeperProperties;
import com.jwk.common.prometheus.service.RegistryService;
import com.jwk.common.prometheus.service.impl.ZookeeperRegistryServiceImpl;
import com.jwk.common.prometheus.support.JwkPrometheusContext;
import com.jwk.common.prometheus.support.JwkPrometheusFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

	@Bean(initMethod = "start")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "jwk.prometheus.registryMode", havingValue = "zookeeper", matchIfMissing = true)
	public CuratorFramework curatorFramework(JwkPrometheusProperties jwkPrometheusProperties) {
		ZookeeperProperties zookeeperInfo = jwkPrometheusProperties.getZookeeper();
		if (StringUtils.isBlank(zookeeperInfo.getAddress())) {
			throw new BeanCreationException("jcloud prometheus address is undefined, please check config");
		}
		if (StringUtils.isBlank(zookeeperInfo.getNamespace())) {
			throw new BeanCreationException("jcloud prometheus namespace is undefined, please check config");
		}
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(zookeeperInfo.getAddress())
				.canBeReadOnly(true).connectionTimeoutMs(zookeeperInfo.getConnectionTimeout())
				.sessionTimeoutMs(zookeeperInfo.getSessionTimeout())
				.retryPolicy(
						new ExponentialBackoffRetry(zookeeperInfo.getRetryIntervalMs(), zookeeperInfo.getRetryNumber()))
				.build();
		return client;
	}

	@Bean
	@ConditionalOnProperty(value = "jwk.prometheus.registryMode", havingValue = "zookeeper", matchIfMissing = true)
	public RegistryService zookeeperRegistryService() {
		return new ZookeeperRegistryServiceImpl();
	}

	@Bean
	public JwkPrometheusContext jwkPrometheusContext() {
		JwkPrometheusContext jwkPrometheusContext = JwkPrometheusFactory.getContext();
		return jwkPrometheusContext;
	}

}
