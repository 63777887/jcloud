package com.jwk.uaa.config;

import cn.hutool.extra.spring.SpringUtil;
import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.security.dto.AdminUserDetails;
import com.jwk.common.security.support.component.*;
import com.jwk.common.security.support.grant.password.PasswordAuthenticationProvider;
import com.jwk.common.security.support.grant.password.PasswordTokenGranter;
import com.jwk.common.security.support.grant.refresh.JwkRefreshAuthenticationProvider;
import com.jwk.common.security.support.grant.refresh.RefreshTokenGranter;
import com.jwk.common.security.support.handler.JwkAuthenticationFailureEventHandler;
import com.jwk.common.security.support.handler.JwkAuthenticationSuccessEventHandler;
import com.jwk.common.security.support.properties.JwkAuthProperties;
import com.jwk.uaa.constant.JwkOAuth2Urls;
import com.jwk.uaa.constant.JwkOIDCConstant;
import com.jwk.uaa.grant.captcha.SmsAuthenticationGranter;
import com.jwk.uaa.grant.captcha.SmsAuthenticationProvider;
import com.jwk.uaa.grant.email.EmailAuthenticationGranter;
import com.jwk.uaa.grant.email.EmailAuthenticationProvider;
import com.jwk.uaa.grant.phone.PhoneAuthenticationGranter;
import com.jwk.uaa.grant.phone.PhoneAuthenticationProvider;
import com.jwk.upms.base.api.UpmsRemoteService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 认证服务器配置
 * @date 2022/11/14
 */
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfiguration {

	private final OAuth2AuthorizationService authorizationService;
	private final StringRedisTemplate stringRedisTemplate;
	private final RedisTemplate redisTemplate;
	private final UpmsRemoteService upmsRemoteService;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

		// 配置个性化认证授权端点（获取accestoken端点）
		// /oauth2/token
		http.apply(authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {
			tokenEndpoint
					// 注入自定义的授权认证Converter
					.accessTokenRequestConverter(accessTokenRequestConverter())
					.accessTokenResponseHandler(new JwkAuthenticationSuccessEventHandler(upmsRemoteService, redisTemplate)) // 登录成功处理器
					// 登录失败处理器
					.errorResponseHandler(new JwkAuthenticationFailureEventHandler());
		}));

		// 个性化客户端认证
		http.apply(authorizationServerConfigurer.clientAuthentication(oAuth2ClientAuthenticationConfigurer ->
		// 处理客户端认证异常
		oAuth2ClientAuthenticationConfigurer.errorResponseHandler(new JwkAuthenticationFailureEventHandler())));

		// 授权码端点配置
		// /oauth2/authorize
		http.apply(authorizationServerConfigurer.authorizationEndpoint(authorizationEndpoint -> {
			// 授权码端点个性化confirm页面
			authorizationEndpoint.consentPage(JwkOAuth2Urls.CUSTOM_CONSENT_PAGE_URI);
			authorizationEndpoint
					.authorizationRequestConverter(new JwkOAuth2AuthorizationCodeRequestAuthenticationConverter());
		}));

		// oidc端点
		authorizationServerConfigurer.oidc(oidc -> {
			oidc.userInfoEndpoint(
					userInfoEndpoint -> userInfoEndpoint.userInfoMapper(oidcUserInfoAuthenticationContext -> {
						UsernamePasswordAuthenticationToken authenticationToken = oidcUserInfoAuthenticationContext
								.getAuthorization().getAttribute(Principal.class.getName());
						Map<String, Object> claims = new HashMap<>();
						AdminUserDetails userDetails = (AdminUserDetails) authenticationToken.getPrincipal();

						claims.put(JwkOIDCConstant.ICON, userDetails.getSysUser().getIcon());
						claims.put(JwkOIDCConstant.USERNAME, userDetails.getSysUser().getUsername());
						claims.put(JwkOIDCConstant.PHONE, userDetails.getSysUser().getPhone());
						return new OidcUserInfo(claims);
					}));
		});
		http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
		// 拿到endpoint需要的端点
		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
		// 配置过滤链拦截的端点（过滤链默认是任意端点，可以通过这个设置，只有匹配中这写端点，才会进入这个过滤链）
		DefaultSecurityFilterChain securityFilterChain = http.requestMatcher(endpointsMatcher)
				// 配置端点的权限（默认提供的oauth2的端点是需要认证权限的
				.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
				.apply(authorizationServerConfigurer
						// redis存储token的实现
						.authorizationService(authorizationService).providerSettings(
								// 配置端点元数据统一发行路径，其中端点路径使用的是默认的（也可以自定义配置，例如.authorizationEndpoint()）
								ProviderSettings.builder().issuer(JwkSecurityConstants.PROJECT_LICENSE).build()))
				// 授权码登录的登录页个性化
				.and().apply(new FormIdentityLoginConfigurer()).and().build();

		// 注入自定义授权模式实现
		addCustomOAuth2GrantAuthenticationProvider(http);
		return securityFilterChain;
	}

	/**
	 * 令牌生成规则实现 </br>
	 * client:username:uuid
	 * @return OAuth2TokenGenerator
	 */
	@Bean
	public OAuth2TokenGenerator oAuth2TokenGenerator() {
		// AccessToken
		JwkOAuth2AccessTokenGenerator accessTokenGenerator = new JwkOAuth2AccessTokenGenerator();
		// 注入Token 增加关联用户信息
		accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());
		// id-token
		JwkOidcTokenGenerator jwtGenerator = new JwkOidcTokenGenerator(new NimbusJwtEncoder(jwkSource()));
		// jwtGenerator.setJwtCustomizer(new CustomeOAuth2TokenCustomizer());
		// new一个token生成器委托器，其中包含自定义accesstoken生成器，id-token和refreshtoken生成器
		return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new JwkOAuth2RefreshTokenGenerator(),
				jwtGenerator);
	}

	@Bean
	@SneakyThrows
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaJwks = getKey();
		JWKSet jwkSet = new JWKSet(Collections.singletonList(rsaJwks));
		return new ImmutableJWKSet<>(jwkSet);
	}

	@Bean
	@SneakyThrows
	public JwtDecoder jwtDecoder() {
		// CertificateFactory certificateFactory =
		// CertificateFactory.getInstance("X.509");
		// ClassPathResource resource = new ClassPathResource("static/jose/pub.cer");
		// Certificate certificate =
		// certificateFactory.generateCertificate(resource.getInputStream());
		// RSAPublicKey rsaPublicKey = (RSAPublicKey) certificate.getPublicKey();
		RSAKey rsaJwks = getKey();
		RSAPublicKey rsaPublicKey = rsaJwks.toRSAPublicKey();
		return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
	}

	/**
	 * 读取ssl密钥，为jwkSource提供服务。
	 * @return
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws JOSEException
	 */
	private RSAKey getKey()
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, JOSEException {
		KeyStore jks = KeyStore.getInstance(JwkOIDCConstant.KEY_STORE_TYPE);
		// 对应keytool命令中的 alias
		String alias = JwkOIDCConstant.KEY_STORE_ALIAS;
		// 对应keytool命令中的 storepass
		String storePass = JwkOIDCConstant.KEY_STORE_STORE_PASS;
		char[] pin = storePass.toCharArray();
		// 借用Spring 读取资源的方法获取密钥文件流
		jks.load(new ClassPathResource(JwkOIDCConstant.KEY_STORE_PATH).getInputStream(), pin);
		return RSAKey.load(jks, alias, pin);
	}

	/**
	 * request -> xToken 注入请求转换器
	 * @return DelegatingAuthenticationConverter
	 */
	private AuthenticationConverter accessTokenRequestConverter() {
		return new DelegatingAuthenticationConverter(Arrays.asList(
				// 手机号码模式
				new PhoneAuthenticationGranter(),
				// 邮箱模式
				new EmailAuthenticationGranter(),
				// 短信模式
				new SmsAuthenticationGranter(stringRedisTemplate),
				// 密码模式
				new PasswordTokenGranter(),

				new RefreshTokenGranter(),
				// 访问令牌请求用于OAuth 2.0刷新令牌授权 ——刷新token
				new OAuth2RefreshTokenAuthenticationConverter(),
				// 客户端模式
				new OAuth2ClientCredentialsAuthenticationConverter(),
				// 授权码模式 ——授权码模式获取token
				new OAuth2AuthorizationCodeAuthenticationConverter(),
				// 授权码模式（确认模式） ——授权码模式获取code
				new JwkOAuth2AuthorizationCodeRequestAuthenticationConverter()));
	}

	/**
	 * 注入授权模式实现提供方
	 * <p>
	 * 1. 密码模式 </br>
	 * 2. 短信登录 </br>
	 */
	@SuppressWarnings("unchecked")
	private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
		// 从shareObject中获取到授权管理业务类（主要负责管理已认证的授权信息）
		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		// 从shareObject中获取到认证管理类
		OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);
		// new一个自定义处理密码模式的授权提供方，其中重点需要注入token生成器
		PasswordAuthenticationProvider passwordAuthenticationProvider = new PasswordAuthenticationProvider(
				authenticationManager, authorizationService, oAuth2TokenGenerator());

		PhoneAuthenticationProvider phoneAuthenticationProvider = new PhoneAuthenticationProvider(authenticationManager,
				authorizationService, oAuth2TokenGenerator());

		EmailAuthenticationProvider emailAuthenticationProvider = new EmailAuthenticationProvider(authenticationManager,
				authorizationService, oAuth2TokenGenerator());

		SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider(authenticationManager,
				authorizationService, oAuth2TokenGenerator());

		JwkRefreshAuthenticationProvider refreshTokenAuthenticationProvider =
				new JwkRefreshAuthenticationProvider(authenticationManager,authorizationService, oAuth2TokenGenerator());

		// 处理 UsernamePasswordAuthenticationToken
		JwkAuthProperties properties = SpringUtil.getBean(JwkAuthProperties.class);
		JwkDaoAuthenticationProvider authenticationProvider = new JwkDaoAuthenticationProvider();
		authenticationProvider.setJwkAuthProperties(properties);
		http.authenticationProvider(authenticationProvider);
		// 处理 PasswordAuthenticationToken
		http.authenticationProvider(passwordAuthenticationProvider);
		// 处理 PhoneAuthenticationToken
		http.authenticationProvider(phoneAuthenticationProvider);
		// 处理 EmailAuthenticationToken
		http.authenticationProvider(emailAuthenticationProvider);
		http.authenticationProvider(smsAuthenticationProvider);
		http.authenticationProvider(refreshTokenAuthenticationProvider);
	}

}
