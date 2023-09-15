package com.jwk.common.prometheus.properties;

import com.jwk.common.prometheus.constant.JwkPrometheusConstants;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

	private boolean enabled;

	/**
	 * 服务名
	 */
	private String application;

	/**
	 * 服务名
	 */
	private String namespace = "jcloud";

	/**
	 * 注册类型
	 */
	private String registryMode = JwkPrometheusConstants.DEFAULT_REGISTER_MODE;

}
