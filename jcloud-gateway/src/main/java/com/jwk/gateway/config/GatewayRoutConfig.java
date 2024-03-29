package com.jwk.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 网关配置项
 * @date 2022/6/11
 */
@Configuration
public class GatewayRoutConfig {

	private static long DEFAULT_READ_TIMEOUT;

	private static String NACOS_DATA_ID;

	private static String NACOS_GROUP_ID;

	public static String getNacosDataId() {
		return NACOS_DATA_ID;
	}

	@Value("${gateway.dynamicRoute.dataId:gateway_platform_routes}")
	public void setNacosDataId(String dataId) {
		NACOS_DATA_ID = dataId;
	}

	public static String getNacosGroupId() {
		return NACOS_GROUP_ID;
	}

	@Value("${gateway.dynamicRoute.group:GATEWAY_PLATFORM}")
	public void setNacosGroupId(String group) {
		NACOS_GROUP_ID = group;
	}

	public static long getDefaultReadTimeout() {
		return DEFAULT_READ_TIMEOUT;
	}

	@Value("${gateway.dynamicRoute.readTimeout:10000}")
	public static void setDefaultReadTimeout(long defaultReadTimeout) {
		DEFAULT_READ_TIMEOUT = defaultReadTimeout;
	}

}