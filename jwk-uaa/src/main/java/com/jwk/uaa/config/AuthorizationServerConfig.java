package com.jwk.uaa.config;

import cn.hutool.jwt.JWT;
import com.jwk.security.constants.JwkSecurityConstants;
import com.jwk.security.security.dto.AdminUserDetails;
import com.jwk.security.security.service.TokenService;
import com.jwk.security.security.service.impl.JwkUserDetailsService;
import com.jwk.uaa.comonpent.JwkAccessTokenConverter;
import com.jwk.uaa.service.JwkClientDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 认证服务器配置
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
@Order(6)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private final DataSource dataSource;

	@Autowired
	private final JwkUserDetailsService jwkUserDetailsService;

	private final AuthenticationManager authenticationManager;

	@Autowired
	JwkClientDetailsService jwkClientDetailsService;


	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private TokenService tokenService;
//	private final RedisConnectionFactory redisConnectionFactory;

	private String SIGNING_KEY = "jiwk";

	@Override
	@SneakyThrows
	public void configure(ClientDetailsServiceConfigurer clients) {
		clients.withClientDetails(jwkClientDetailsService);
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

		// 增强器链
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter(),tokenEnhancer()));

		endpoints
				// 配置请求方式
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				// JWT令牌存储方案
				.tokenStore(tokenStore())
				// token增强
				.tokenEnhancer(enhancerChain)
				//	userDetailsService用户检验用户
				.userDetailsService(jwkUserDetailsService)
				//	认证管理器
				.authenticationManager(authenticationManager)
				//	服务器端点配置器重用刷新令牌
				.reuseRefreshTokens(true)
//				.tokenServices(tokenServices(endpoints))
				// 授权码保存方式设为数据库保存
				.authorizationCodeServices(authorizationCodeServices());
//				// 	路径映射
//				.pathMapping("/oauth/confirm_access", "/token/confirm_access")
				//	异常翻译器
//				.exceptionTranslator(new JwkWebResponseExceptionTranslator());
	}

	// 保存授权码由内存的方式改为数据库的方式
	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(dataSource);
	}

	//JWT令牌存储方案
	@Bean
	public TokenStore tokenStore() {
		// 数据库存储
//		return new JdbcTokenStore(dataSource);
		// 内存存储
//		return new InMemoryTokenStore();
		// redis存储
//		return new RedisTokenStore(redisConnectionFactory);
//		RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
//		tokenStore.setPrefix(CacheConstants.PROJECT_OAUTH_ACCESS);
		return new JwtTokenStore(accessTokenConverter());
	}


//	private AuthorizationServerTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
//		DefaultTokenServices tokenServices = new DefaultTokenServices();
//		tokenServices.setTokenStore(tokenStore());
//		//支持刷新token
//		tokenServices.setSupportRefreshToken(true);
//		tokenServices.setReuseRefreshToken(true);
//		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
//		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
//		tokenServices.setAuthenticationManager(authenticationManager);
//		return tokenServices;
//	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		//JwtAccessTokenConverter是用来生成token的转换器，而token令牌默认是有签名的，且资源服务器需要验证这个签名。
		// 此处的加密及验签包括两种方式：对称加密、非对称加密（公钥密钥）
		JwkAccessTokenConverter converter = new JwkAccessTokenConverter();
		converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证
		return converter;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		//	TokenEnhancer向jwt中添加额外信息
		return (accessToken, authentication) -> {
			final Map<String, Object> additionalInfo = new HashMap<>(2);
			Object principal = authentication.getPrincipal();
			String username = "";
			// 颁发令牌和刷新令牌时，authentication存储的信息不一致
			if (principal instanceof AdminUserDetails){
				username = ((AdminUserDetails) principal).getUsername();
			}
			if (principal instanceof String){
				username = (String)principal ;
			}
			additionalInfo.put(JwkSecurityConstants.DETAILS_USERNAME, username);
			additionalInfo.put(JwkSecurityConstants.DETAILS_LICENSE, JwkSecurityConstants.PROJECT_LICENSE);
			DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
			Map<String, ?> stringMap = defaultAccessTokenConverter
					.convertAccessToken(accessToken, authentication);
			additionalInfo.putAll(stringMap);
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

			HashMap<String, Object> claimsParam = new HashMap<>();
			stringMap.forEach(claimsParam::put);
			Claims claims = new DefaultClaims(claimsParam);
			claims.setSubject(username);
			// 设置token的值
			String token ;
			Object exp = JWT.of(accessToken.getValue())
					.getPayload("exp");
			if (null != exp){
				System.out.println("==================="+exp+"===================");
				token = tokenService.generateToken(claims,new Date((Integer) exp*1000L));
			}else {
				token = tokenService.generateToken(username);
			}
			((DefaultOAuth2AccessToken) accessToken).setValue(token);
			return accessToken;
		};
	}

}
