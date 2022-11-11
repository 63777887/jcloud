package com.jwk.common.prometheus.properties;

import lombok.Data;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Zookeeper配置信息
 */
@Data
public class ZookeeperProperties {

	/**
	 * 优先级: address > addressEnv
	 */
	private String address = System.getenv("ZOOKEEPER_URL");

	/**
	 * 服务注册namespace
	 */
	private String namespace = System.getenv("PROMETHEUS_SERVICE_NAMESPACE");

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
