package com.jwk.common.security.security.component;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.cloud.commons.security.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 注入AccessTokenContextRelay 解决feign 传递token 为空问题
 */
@AutoConfigureAfter(OAuth2AutoConfiguration.class)
@ConditionalOnWebApplication
@ConditionalOnProperty("security.oauth2.client.client-id")
public class JwkResourceServerTokenRelayAutoConfiguration {

	@Bean
	public AccessTokenContextRelay accessTokenContextRelay(OAuth2ClientContext context) {
		return new AccessTokenContextRelay(context);
	}

}
