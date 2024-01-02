package com.jwk.uaa.grant.captcha;

import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import com.jwk.uaa.constant.JwkOAuth2ParameterNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 邮箱认证Provider
 * @date 2022/6/11
 */
@Slf4j
public class SmsAuthenticationProvider
		extends OAuth2ResourceOwnerBaseAuthenticationProvider<SmsAuthenticationToken> {

	public SmsAuthenticationProvider(AuthenticationManager authenticationManager,
									 OAuth2AuthorizationService authorizationService,
									 OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		super(authenticationManager, authorizationService, tokenGenerator);
	}

	@Override
	public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
		String phone = (String) reqParameters.get(JwkOAuth2ParameterNames.PHONE_PARAMETER_NAME);
		return new UsernamePasswordAuthenticationToken(phone, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		boolean supports = SmsAuthenticationToken.class.isAssignableFrom(authentication);
		if (log.isDebugEnabled()) {
			log.debug("supports authentication=" + authentication + " returning " + supports);
		}
		return supports;
	}

	@Override
	public void checkClient(RegisteredClient registeredClient) {
		assert registeredClient != null;
		if (!registeredClient.getAuthorizationGrantTypes()
				.contains(new AuthorizationGrantType(JwkOAuth2ParameterNames.SMS))) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}
	}

}