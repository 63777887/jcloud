package com.jwk.common.zookeeper.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Jiwk
 * @date 2023/9/15
 * @version 0.1.4
 * <p>
 * Zookeeper配置信息
 */
@Data
@ConfigurationProperties(prefix = "jwk.zookeeper")
public class JwkZookeeperProperties {

	private boolean enabled;

	/**
	 * 优先级: address > addressEnv
	 */
	private String address = System.getenv("ZOOKEEPER_URL");

	/**
	 * session超时时间
	 */
	private int sessionTimeout = 100 * 1000;

	/**
	 * 连接超时时间
	 */
	private int connectionTimeout = 50 * 1000;

	/**
	 * 重试间隔
	 */
	private int retryIntervalMs = 3 * 1000;

	/**
	 * 重试次数
	 */
	private int retryNumber = 5;

}
