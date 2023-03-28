package com.jwk.common.security.config;

import com.jwk.common.security.support.component.JwkBearerTokenExtractor;
import com.jwk.common.security.support.handler.ResourceAccessDeniedHandler;
import com.jwk.common.security.support.handler.ResourceAuthExceptionEntryPoint;
import com.jwk.common.security.support.properties.JwkAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 资源服务器认证授权配置
 * @date 2022/11/13
 */
@Slf4j
@RequiredArgsConstructor
public class JwkResourceServerConfiguration {

	protected final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

	protected final ResourceAccessDeniedHandler resourceAccessDeniedHandler;

	private final JwkAuthProperties permitAllUrl;

	private final JwkBearerTokenExtractor jwkBearerTokenExtractor;

	private final OpaqueTokenIntrospector customOpaqueTokenIntrospector;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// 配置端点白名单方放行
		// 配置资源服务信息
		// 配置accesstoken提取器
		// 配置token自省器
		// 忽略掉相关端点的 csrf

		// 配置端点白名单方放行
		http.authorizeRequests(authorizeRequests -> authorizeRequests.antMatchers(permitAllUrl.getNoAuthArray())
				.permitAll().anyRequest().authenticated())
				// 进行资源服务器配置
				.oauth2ResourceServer(oauth2 ->
				// 令牌认证
				oauth2.opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))
						// 自定义认证异常
						.authenticationEntryPoint(resourceAuthExceptionEntryPoint)
						// 自定义权限异常
						.accessDeniedHandler(resourceAccessDeniedHandler)
						// 转化request，拿到token
						.bearerTokenResolver(jwkBearerTokenExtractor))
				.headers().frameOptions().disable().and().csrf().disable();

		return http.build();
	}

}
