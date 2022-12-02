package com.jwk.uaa.grant.email;

import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import com.jwk.uaa.constant.JwkOAuth2ParameterNames;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 邮箱认证Provider
 */
@Slf4j
public class EmailAuthenticationProvider
		extends OAuth2ResourceOwnerBaseAuthenticationProvider<EmailAuthenticationToken> {

	public EmailAuthenticationProvider(AuthenticationManager authenticationManager,
			OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		super(authenticationManager, authorizationService, tokenGenerator);
	}

	@Override
	public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
		String username = (String) reqParameters.get(JwkOAuth2ParameterNames.EMAIL_PARAMETER_NAME);
		String password = (String) reqParameters.get(OAuth2ParameterNames.PASSWORD);
		return new UsernamePasswordAuthenticationToken(username, password);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		boolean supports = EmailAuthenticationToken.class.isAssignableFrom(authentication);
		if (log.isDebugEnabled()) {
			log.debug("supports authentication=" + authentication + " returning " + supports);
		}
		return supports;
	}

	@Override
	public void checkClient(RegisteredClient registeredClient) {
		assert registeredClient != null;
		if (!registeredClient.getAuthorizationGrantTypes().
				contains(new AuthorizationGrantType(JwkOAuth2ParameterNames.EMAIL))) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}
	}

}