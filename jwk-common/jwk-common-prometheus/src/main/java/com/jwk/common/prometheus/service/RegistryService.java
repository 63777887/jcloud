package com.jwk.common.prometheus.service;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 注册中心接口
 */
public interface RegistryService {

	/**
	 * 注册中心注册
	 */
	void registry();

	/**
	 * 支持的格式
	 * @return
	 */
	String support();

}
