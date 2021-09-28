package com.jwk.security.security.conf;

import com.jwk.security.security.component.JwkAuthProperties;
import com.jwk.security.security.component.JwkUserAuthenticationConverter;
import com.jwk.security.security.handler.JwkAuthenticationFailHandler;
import com.jwk.security.security.handler.JwtForbiddenConfigHandler;
import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 1. 支持remoteTokenServices 负载均衡 2. 支持 获取用户全部信息 3. 接口对外暴露，不校验 Authentication Header 头
 */
@Slf4j
//@Configuration
//@Order(1)
public class JwkResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {


//	@Autowired
//	protected RemoteTokenServices remoteTokenServices;

	@Autowired
	private JwkAuthProperties jwkAuthProperties;

	@Autowired
	private RestTemplate lbRestTemplate;

  @Autowired
  private AutowireCapableBeanFactory autowireCapableBeanFactory;

  @Autowired
  private ResourceServerProperties resourceServerProperties;


	/**
	 * 默认的配置，对外暴露
	 * @param httpSecurity
	 */

	@Override
	@SneakyThrows
	public void configure(HttpSecurity httpSecurity) {
		/**
		 * 微服务的Http安全配置
		 */
		String[] noAuthUrlList = jwkAuthProperties.getNoauthUrl().split(",");
		// 允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
		httpSecurity.headers().frameOptions().disable();
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
				.authorizeRequests();
		Arrays.stream(noAuthUrlList).forEach(url -> registry.antMatchers(url).permitAll());
		registry.anyRequest().authenticated().and().csrf().disable();


//    // 添加自定义的jwt过滤器
//    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
//    // 注入属性
//    autowireCapableBeanFactory.autowireBean(jwtAuthenticationFilter);
//    // 如果使用addFilter 则会抛异常，没有指定order
//		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
    //添加自定义的权限过滤器
    DynamicResourceFilter dynamicResourceFilter = new DynamicResourceFilter();
    autowireCapableBeanFactory.autowireBean(dynamicResourceFilter);
		httpSecurity.addFilterBefore(dynamicResourceFilter, FilterSecurityInterceptor.class);
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {

		resources.
				authenticationEntryPoint(new JwkAuthenticationFailHandler()).
				tokenExtractor(new JwkBearerTokenExtractor(jwkAuthProperties)).
				tokenStore(tokenStore()).
				accessDeniedHandler(new JwtForbiddenConfigHandler()).
				tokenServices(remoteTokenServices());

//
	}


	public RemoteTokenServices remoteTokenServices() {
		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		UserAuthenticationConverter userTokenConverter = new JwkUserAuthenticationConverter();
		accessTokenConverter.setUserTokenConverter(userTokenConverter);
		RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
		remoteTokenServices.setRestTemplate(lbRestTemplate);
		remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
		remoteTokenServices.setClientId(resourceServerProperties.getClientId());
		remoteTokenServices.setClientSecret(resourceServerProperties.getClientSecret());
		remoteTokenServices.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());

		return remoteTokenServices;
	}


	/**
	 * 创建令牌存储对象
	 */
	public TokenStore tokenStore() {
		/**
		 * 使用JwtTokenStore时，必须注入一个JwtAccessTokenConverter，用于解析JWT令牌
		 */
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	/**
	 * 创建JWT令牌转换器
	 */
	public JwtAccessTokenConverter jwtAccessTokenConverter(){
		/**
		 * 设置JWT令牌的签名key
		 */
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("jiwk");
		return converter;
	}




}
