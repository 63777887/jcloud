package com.jwk.common.prometheus.component;

import com.jwk.common.prometheus.constant.JwkPrometheusConstants;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 配置文件
 */
@Data
@ConfigurationProperties(prefix = "jwk.prometheus")
public class JwkPrometheusProperties {

	protected final static Logger logger = LoggerFactory.getLogger(JwkPrometheusProperties.class);

	/**
	 * 服务名
	 */
	private String application;

	private String registryMode = JwkPrometheusConstants.DEFAULT_REGISTER_MODE;

	@NestedConfigurationProperty
	private ZookeeperProperties zookeeper;

}