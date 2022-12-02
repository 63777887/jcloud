package com.jwk.common.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwk.common.security.support.component.JwkCustomOpaqueTokenIntrospector;
import com.jwk.common.security.support.properties.JwkAuthProperties;
import com.jwk.common.security.support.component.JwkBearerTokenExtractor;
import com.jwk.common.security.support.component.PermissionService;
import com.jwk.common.security.support.handler.ResourceAccessDeniedHandler;
import com.jwk.common.security.support.handler.ResourceAuthExceptionEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * @author Jiwk
 * @date 2022/11/14
 * @version 0.1.4
 * <p>
 * 资源服务器配置
 */
public class JwkResourceServerAutoConfiguration {

	 /**
	 * 鉴权具体的实现逻辑
	 * @return （#pms.xxx）
	 */
	 @Bean("pms")
	 public PermissionService permissionService() {
	 return new PermissionService();
	 }

	/**
	 * 从请求中获取令牌
	 * @param urlProperties 无需鉴权的列表
	 * @return BearerTokenExtractor
	 */
	@Bean
	public JwkBearerTokenExtractor jwkBearerTokenExtractor(JwkAuthProperties urlProperties) {
		return new JwkBearerTokenExtractor(urlProperties);
	}

	/**
	 * 资源服务器异常处理
	 * @param objectMapper jackson 输出对象
	 * @return ResourceAuthExceptionEntryPoint
	 */
	@Bean
	public ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint(ObjectMapper objectMapper) {
		return new ResourceAuthExceptionEntryPoint(objectMapper);
	}

	/**
	 * 资源服务器异常处理
	 * @param objectMapper jackson 输出对象
	 * @return ResourceAuthExceptionEntryPoint
	 */
	@Bean
	public ResourceAccessDeniedHandler resourceAccessDeniedHandler(ObjectMapper objectMapper) {
		return new ResourceAccessDeniedHandler(objectMapper);
	}

	/**
	 * 令牌认证解析
	 * @param authorizationService token 存储实现
	 * @return TokenIntrospector
	 */
	@Bean
	public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
		return new JwkCustomOpaqueTokenIntrospector(authorizationService);
	}

}
