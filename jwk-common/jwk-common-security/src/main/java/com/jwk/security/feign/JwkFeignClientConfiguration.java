package com.jwk.security.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.commons.security.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * feign 拦截器传递 header 中oauth token， 使用hystrix 的信号量模式
 */
@ConditionalOnProperty("security.oauth2.client.client-id")
public class JwkFeignClientConfiguration {


	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor(
			@Qualifier("oauth2ClientContext") OAuth2ClientContext oAuth2ClientContext,
			OAuth2ProtectedResourceDetails resource, AccessTokenContextRelay accessTokenContextRelay) {
		return new JwkFeignClientInterceptor(oAuth2ClientContext, resource, accessTokenContextRelay);
	}

}
