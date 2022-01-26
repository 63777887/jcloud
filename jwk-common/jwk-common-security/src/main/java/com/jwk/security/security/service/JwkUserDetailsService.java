package com.jwk.security.security.service;

import com.jwk.security.web.entity.SysUser;
import org.springframework.core.Ordered;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface JwkUserDetailsService extends UserDetailsService, Ordered {


	/**
	 * 是否支持此客户端校验
	 * @param clientId 目标客户端
	 * @return true/false
	 */
	default boolean support(String clientId) {
		return true;
	}

	/**
	 * 是否支持此登录方式
	 * @param grantType 目标客户端
	 * @return true/false
	 */
	default boolean supportGrantType(String grantType) {
		return true;
	}

	/**
	 * 排序值 默认取最大的
	 * @return 排序值
	 */
	default int getOrder() {
		return 0;
	}

}
