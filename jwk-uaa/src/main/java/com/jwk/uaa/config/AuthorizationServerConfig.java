/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jwk.uaa.config;


import com.jwk.security.constants.JwkSecurityConstants;
import com.jwk.security.security.dto.AdminUserDetails;
import com.jwk.security.security.service.TokenService;
import com.jwk.security.security.service.impl.JwkUserDetailsService;
import com.jwk.security.web.entity.SysUser;
import com.jwk.uaa.component.JwkWebResponseExceptionTranslator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author lengleng
 * @date 2019/2/1 认证服务器配置
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
@Order(6)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private final DataSource dataSource;

	private final JwkUserDetailsService jwkUserDetailsService;

	private final AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@Autowired
	PasswordEncoder passwordEncoder;
//	private final RedisConnectionFactory redisConnectionFactory;

	private String SIGNING_KEY = "jiwk";

	@Override
	@SneakyThrows
	public void configure(ClientDetailsServiceConfigurer clients) {
		ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
		clients.withClientDetails(JwkClientDetailsService());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		//	配置AuthorizationServer的端点（/oauth/****）的安全访问规则
		oauthServer.allowFormAuthenticationForClients()			//表单认证（申请令牌）
								.checkTokenAccess("permitAll()")				//oauth/check_token公开
								.tokenKeyAccess("permitAll()");					//oauth/token_key是公开
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		//	AuthorizationServerEndpointsConfigurer其实是一个装载类，装载Endpoints所有相关的类配置
		//	（AuthorizationServer、TokenServices、TokenStore、ClientDetailsService、UserDetailsService）
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				//	userDetailsService
				.userDetailsService(jwkUserDetailsService)
				//	认证管理器
				.authenticationManager(authenticationManager)
				//	服务器端点配置器重用刷新令牌
				.reuseRefreshTokens(false)
				//	令牌管理服务
				.tokenServices(serverTokenService())
//				// 	路径映射
//				.pathMapping("/oauth/confirm_access", "/token/confirm_access")
				//	异常翻译器
				.exceptionTranslator(new JwkWebResponseExceptionTranslator());
	}

	@Bean
	public ClientDetailsService JwkClientDetailsService(){
//		//	配置JdbcClientDetailsService，用于查询Client信息
//		JwkClientDetailsService jwkClientDetailsService = new JwkClientDetailsService(dataSource);
//		//	设置默认根据client_id查询Client语句
//		jwkClientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
//		//	设置默认的查询所有Client语句
//		jwkClientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);

		JdbcClientDetailsService jwkClientDetailsService = new JdbcClientDetailsService(dataSource);
		jwkClientDetailsService.setPasswordEncoder(passwordEncoder);
		return jwkClientDetailsService;
	}

	//令牌管理服务
	@Bean
	public AuthorizationServerTokenServices serverTokenService() {
		DefaultTokenServices service=new DefaultTokenServices();
		service.setClientDetailsService(JwkClientDetailsService());//客户端详情服务
		service.setSupportRefreshToken(true);//支持刷新令牌
		service.setTokenStore(tokenStore());//令牌存储策略
		//令牌增强器,往令牌中增加其他信息
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));
		service.setTokenEnhancer(tokenEnhancerChain);

		service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
		service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
		return service;
	}

	@Bean
	public TokenStore tokenStore() {
		//JWT令牌存储方案
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		//JwtAccessTokenConverter是用来生成token的转换器，而token令牌默认是有签名的，且资源服务器需要验证这个签名。
		// 此处的加密及验签包括两种方式：对称加密、非对称加密（公钥密钥）
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证
		return converter;
	}

//	@Bean
//	public TokenStore tokenStore() {
//		RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
//		tokenStore.setPrefix(CacheConstants.PROJECT_OAUTH_ACCESS);
//		return tokenStore;
//	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		//	TokenEnhancer向jwt中添加额外信息
		return (accessToken, authentication) -> {
			final Map<String, Object> additionalInfo = new HashMap<>(4);
			AdminUserDetails userDetails = (AdminUserDetails) authentication.getUserAuthentication().getDetails();
			SysUser sysUser = userDetails.getSysUser();
			additionalInfo.put(JwkSecurityConstants.DETAILS_LICENSE, JwkSecurityConstants.PROJECT_LICENSE);
			additionalInfo.put(JwkSecurityConstants.DETAILS_USERNAME, sysUser.getUsername());
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
			((DefaultOAuth2AccessToken) accessToken).setValue(tokenService.generateToken(sysUser.getUsername()));
			return accessToken;
		};
	}


}
