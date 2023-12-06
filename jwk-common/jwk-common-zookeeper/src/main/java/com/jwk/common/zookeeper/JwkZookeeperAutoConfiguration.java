package com.jwk.common.zookeeper;

import cn.hutool.core.util.StrUtil;
import com.jwk.common.zookeeper.properties.JwkZookeeperProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Jiwk
 * @date 2023/9/15
 * @version 0.1.4
 * <p>
 * 自动配置类
 */
@EnableConfigurationProperties(JwkZookeeperProperties.class)
@ConditionalOnProperty(prefix = "jwk.zookeeper", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JwkZookeeperAutoConfiguration {

	private static Logger logger = LoggerFactory.getLogger(JwkZookeeperAutoConfiguration.class);

	@Bean(initMethod = "start")
	@ConditionalOnMissingBean
	public CuratorFramework curatorFramework(JwkZookeeperProperties jwkZookeeperProperties) {

		if (StrUtil.isBlank(jwkZookeeperProperties.getAddress())) {
			throw new BeanCreationException("zookeeper address is undefined, please check config");
		}

		return CuratorFrameworkFactory.builder().connectString(jwkZookeeperProperties.getAddress()).canBeReadOnly(true)
				.connectionTimeoutMs(jwkZookeeperProperties.getConnectionTimeout())
				.sessionTimeoutMs(jwkZookeeperProperties.getSessionTimeout())
				.retryPolicy(new ExponentialBackoffRetry(jwkZookeeperProperties.getRetryIntervalMs(),
						jwkZookeeperProperties.getRetryNumber()))
				.build();
	}

}
