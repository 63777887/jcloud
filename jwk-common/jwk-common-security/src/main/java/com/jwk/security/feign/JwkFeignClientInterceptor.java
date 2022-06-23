package com.jwk.security.feign;

import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.commons.security.AccessTokenContextRelay;
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 扩展OAuth2FeignRequestInterceptor
 */
@Slf4j
public class JwkFeignClientInterceptor extends OAuth2FeignRequestInterceptor {

	private final OAuth2ClientContext oAuth2ClientContext;

	private final AccessTokenContextRelay accessTokenContextRelay;

	/**
	 * Default constructor which uses the provided OAuth2ClientContext and Bearer tokens
	 * within Authorization header
	 * @param oAuth2ClientContext provided context
	 * @param resource type of resource to be accessed
	 * @param accessTokenContextRelay
	 */
	public JwkFeignClientInterceptor(OAuth2ClientContext oAuth2ClientContext, OAuth2ProtectedResourceDetails resource,
			AccessTokenContextRelay accessTokenContextRelay) {
		super(oAuth2ClientContext, resource);
		this.oAuth2ClientContext = oAuth2ClientContext;
		this.accessTokenContextRelay = accessTokenContextRelay;
	}

	/**
	 * Create a template with the header of provided name and extracted extract 1. 如果使用
	 * 非web 请求，header 区别 2. 根据authentication 还原请求token
	 * @param template
	 */
	@Override
	public void apply(RequestTemplate template) {

		accessTokenContextRelay.copyToken();
		if (oAuth2ClientContext != null && oAuth2ClientContext.getAccessToken() != null) {
			super.apply(template);
		}
	}

}
