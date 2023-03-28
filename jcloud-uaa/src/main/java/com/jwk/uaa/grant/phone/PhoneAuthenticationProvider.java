package com.jwk.uaa.grant.phone;

import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import com.jwk.uaa.constant.JwkOAuth2ParameterNames;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 短信登录Provider
 * @date 2022/11/24
 */
public class PhoneAuthenticationProvider
		extends OAuth2ResourceOwnerBaseAuthenticationProvider<PhoneAuthenticationToken> {

	private static final Logger LOGGER = LogManager.getLogger(PhoneAuthenticationProvider.class);

	public PhoneAuthenticationProvider(AuthenticationManager authenticationManager,
			OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		super(authenticationManager, authorizationService, tokenGenerator);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		boolean supports = PhoneAuthenticationToken.class.isAssignableFrom(authentication);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("supports authentication=" + authentication + " returning " + supports);
		}
		return supports;
	}

	@Override
	public void checkClient(RegisteredClient registeredClient) {
		assert registeredClient != null;
		if (!registeredClient.getAuthorizationGrantTypes()
				.contains(new AuthorizationGrantType(JwkOAuth2ParameterNames.PHONE))) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}
	}

	@Override
	public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
		String phone = (String) reqParameters.get(JwkOAuth2ParameterNames.PHONE_PARAMETER_NAME);
		String password = (String) reqParameters.get(OAuth2ParameterNames.PASSWORD);
		return new UsernamePasswordAuthenticationToken(phone, password);
	}

}
