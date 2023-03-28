package com.jwk.common.security.config;

import com.jwk.common.security.support.feign.JwkOAuthRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * feign 客户端配置
 * @date 2022/11/24
 */
public class JwkFeignClientConfiguration {

	/**
	 * 注入 oauth2 feign token 增强
	 * @param tokenResolver token获取处理器
	 * @return 拦截器
	 */
	@Bean
	public RequestInterceptor oauthRequestInterceptor(BearerTokenResolver tokenResolver) {
		return new JwkOAuthRequestInterceptor(tokenResolver);
	}

}
