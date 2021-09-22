package com.jwk.security.security.conf;

import com.jwk.security.security.component.JwkAuthProperties;
import com.jwk.security.security.component.JwkUserAuthenticationConverter;
import com.jwk.security.security.handler.AuthenticationFailHandler;
import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 1. 支持remoteTokenServices 负载均衡 2. 支持 获取用户全部信息 3. 接口对外暴露，不校验 Authentication Header 头
 */
@Slf4j
public class PigResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {

	@Autowired
	protected AuthenticationFailHandler resourceAuthExceptionEntryPoint;

	@Autowired
	protected RemoteTokenServices remoteTokenServices;

	@Autowired
	private AccessDeniedHandler pigAccessDeniedHandler;

	@Autowired
	private JwkAuthProperties permitAllUrl;

	@Autowired
	private RestTemplate lbRestTemplate;

	@Autowired
	private JwkBearerTokenExtractor pigBearerTokenExtractor;

	/**
	 * 默认的配置，对外暴露
	 * @param httpSecurity
	 */
	@Override
	@SneakyThrows
	public void configure(HttpSecurity httpSecurity) {
		// 允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
		httpSecurity.headers().frameOptions().disable();
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
				.authorizeRequests();
		Arrays.stream(permitAllUrl.getNoauthUrl().split(",")).forEach(url -> registry.antMatchers(url).permitAll());
		registry.anyRequest().authenticated().and().csrf().disable();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		UserAuthenticationConverter userTokenConverter = new JwkUserAuthenticationConverter();
		accessTokenConverter.setUserTokenConverter(userTokenConverter);

		remoteTokenServices.setRestTemplate(lbRestTemplate);
		remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
		// 指定checkToken地址，用于远程调用授权服务校验token
//		remoteTokenServices.setCheckTokenEndpointUrl("/check_token");
		resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint).tokenExtractor(pigBearerTokenExtractor)
				.accessDeniedHandler(pigAccessDeniedHandler).tokenServices(remoteTokenServices);
	}


}
