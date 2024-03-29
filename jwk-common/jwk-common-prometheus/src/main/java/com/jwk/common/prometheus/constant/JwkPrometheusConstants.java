package com.jwk.common.prometheus.constant;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Prometheus常量
 */
public interface JwkPrometheusConstants {

	/**
	 * Prometheus Counter 前缀
	 */
	String JCLOUD_SERVER_FEIGN_METHOD_COUNT = "feign_method_count";

	/**
	 * Prometheus Timer 前缀
	 */
	String JCLOUD_SERVER_FEIGN_METHOD_TIMER = "feign_method_timer";

	String APPLICATION = "application";

	/**
	 * Tag method
	 */
	String TAG_METHOD = "method";

	/**
	 * Tag status
	 */
	String TAG_STATUS = "status";

	/**
	 * Counter 说明
	 */
	String COUNTER_DESC = "接口调用次数统计";

	/**
	 * Timer 说明
	 */
	String TIMER_DESC = "接口调用时间统计";

	String P = "/";

	String DEFAULT_REGISTER_MODE = "zookeeper";

}
