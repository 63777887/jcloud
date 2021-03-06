package com.jwk.uaa.config;

import com.jwk.common.security.constants.JwkSecurityConstants;
import com.jwk.common.security.security.component.JwkAuthProperties;
import com.jwk.common.security.security.component.JwkPreAuthenticatedAuthenticationProvider;
import com.jwk.common.security.security.component.JwkUserAuthenticationConverter;
import com.jwk.common.security.security.component.JwkWebResponseExceptionTranslator;
import com.jwk.common.security.security.dto.AdminUserDetails;
import com.jwk.common.security.security.grant.ResourceOwnerPhoneTokenGranter;
import com.jwk.common.security.security.service.JwkUserDetailsService;
import com.jwk.uaa.service.JwkClientDetailsService;
import java.util.ArrayList;
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
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * ?????????????????????
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
@Order(6)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private final DataSource dataSource;

	@Autowired
	private final JwkUserDetailsService jwkUserDetailsService;

	@Autowired
	private final AuthenticationManager authenticationManager;

	@Autowired
	JwkClientDetailsService jwkClientDetailsService;

	@Autowired
	JwkAuthProperties jwkAuthProperties;


	@Autowired
	PasswordEncoder passwordEncoder;

//	private final RedisConnectionFactory redisConnectionFactory;

	@Override
	@SneakyThrows
	public void configure(ClientDetailsServiceConfigurer clients) {
		clients.withClientDetails(jwkClientDetailsService);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		//	??????AuthorizationServer????????????/oauth/****????????????????????????
								//??????????????????????????????
		oauthServer.allowFormAuthenticationForClients()
								//oauth/check_token??????
								.checkTokenAccess("permitAll()")
								//oauth/token_key?????????
								.tokenKeyAccess("permitAll()");

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		//	AuthorizationServerEndpointsConfigurer?????????????????????????????????Endpoints????????????????????????

		// ????????????
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter(),tokenEnhancer()));

		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		UserAuthenticationConverter userTokenConverter = new JwkUserAuthenticationConverter();
		accessTokenConverter.setUserTokenConverter(userTokenConverter);
		endpoints
				// ??????????????????
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				.tokenServices(tokenServices(endpoints))
				//	???????????????
				.authenticationManager(authenticationManager)
				//	??????????????????????????????????????????
				.reuseRefreshTokens(true)
				// ??????????????????????????????????????????
				.authorizationCodeServices(authorizationCodeServices())
				.accessTokenConverter(accessTokenConverter)
//				// 	????????????
//				.pathMapping("/oauth/confirm_access", "/token/confirm_access")
				//	???????????????
				.exceptionTranslator(new JwkWebResponseExceptionTranslator());
		setTokenGranter(endpoints);
	}



	private void addUserDetailsService(
			DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
		if (userDetailsService != null) {
			JwkPreAuthenticatedAuthenticationProvider provider = new JwkPreAuthenticatedAuthenticationProvider();
			provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>(
					userDetailsService));
			tokenServices
					.setAuthenticationManager(new ProviderManager(Arrays.<AuthenticationProvider> asList(provider)));
		}
	}

	private void setTokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
		// ????????????????????????
		TokenGranter tokenGranter = endpoints.getTokenGranter();
		ArrayList<TokenGranter> tokenGranters = new ArrayList<>(Arrays.asList(tokenGranter));
		ResourceOwnerPhoneTokenGranter resourceOwnerPhoneTokenGranter = new ResourceOwnerPhoneTokenGranter(authenticationManager,
				endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
		tokenGranters.add(resourceOwnerPhoneTokenGranter);
		CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(tokenGranters);
		endpoints.tokenGranter(compositeTokenGranter);
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * @return
	 */
	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(dataSource);
	}


	/**
	 * JWT??????????????????
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		// ???????????????
//		return new JdbcTokenStore(dataSource);
		// ????????????
//		return new InMemoryTokenStore();
		// redis??????
//		return new RedisTokenStore(redisConnectionFactory);
//		RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
//		tokenStore.setPrefix(CacheConstants.PROJECT_OAUTH_ACCESS);
		return new JwtTokenStore(accessTokenConverter());
	}


	private AuthorizationServerTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		// ????????????
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter(),tokenEnhancer()));

		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		UserAuthenticationConverter userTokenConverter = new JwkUserAuthenticationConverter();
		accessTokenConverter.setUserTokenConverter(userTokenConverter);
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setReuseRefreshToken(true);
		tokenServices.setAuthenticationManager(authenticationManager);
		tokenServices.setClientDetailsService(jwkClientDetailsService);
		tokenServices.setTokenEnhancer(enhancerChain);
		addUserDetailsService(tokenServices, jwkUserDetailsService);
		return tokenServices;
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		//JwtAccessTokenConverter???????????????token??????????????????token???????????????????????????????????????????????????????????????????????????
		// ?????????????????????????????????????????????????????????????????????????????????????????????
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		//??????????????????????????????????????????????????????
		converter.setSigningKey(jwkAuthProperties.getSecretKey());
		// ?????????token??????
		DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
		defaultAccessTokenConverter.setUserTokenConverter(new JwkUserAuthenticationConverter());
		converter.setAccessTokenConverter(defaultAccessTokenConverter);
		return converter;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		//	TokenEnhancer???jwt?????????????????????
		return (accessToken, authentication) -> {
			final Map<String, Object> additionalInfo = new HashMap<>(2);
			Object principal = authentication.getPrincipal();
			String username = "";
			Long userId = -1L;
			// ?????????????????????????????????authentication????????????????????????
			Object details = authentication.getUserAuthentication().getDetails();
			if (details instanceof AdminUserDetails){
				username = ((AdminUserDetails) details).getUsername();
				userId = ((AdminUserDetails) details).getSysUser().getId();
			}
			additionalInfo.put(JwkSecurityConstants.DETAILS_USERNAME, username);
			additionalInfo.put(JwkSecurityConstants.DETAILS_USER_ID, userId);
			additionalInfo.put(JwkSecurityConstants.DETAILS_LICENSE, JwkSecurityConstants.PROJECT_LICENSE);
//			DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
//			Map<String, ?> stringMap = defaultAccessTokenConverter
//					.convertAccessToken(accessToken, authentication);
//			additionalInfo.putAll(stringMap);
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

//			HashMap<String, Object> claimsParam = new HashMap<>();
//			stringMap.forEach(claimsParam::put);
//			Claims claims = new DefaultClaims(claimsParam);
//			claims.setSubject(username);
//			// ??????token??????
//			String token ;
//			Object exp = JWT.of(accessToken.getValue())
//					.getPayload(AccessTokenConverter.EXP);
//			if (null != exp){
//				token = tokenService.generateToken(claims,new Date((Integer) exp*1000L));
//			}else {
//				token = tokenService.generateToken(username);
//			}
//			((DefaultOAuth2AccessToken) accessToken).setValue(token);
			return accessToken;
		};
	}

}
