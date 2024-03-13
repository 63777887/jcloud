package com.jwk.common.security.support.grant.refresh;

import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
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

import java.util.Map;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 密码认证Provider
 * @date 2022/6/11
 */
@Slf4j
public class JwkRefreshAuthenticationProvider
		extends OAuth2ResourceOwnerBaseAuthenticationProvider<RefreshAuthenticationToken> {

	public JwkRefreshAuthenticationProvider(AuthenticationManager authenticationManager,
											OAuth2AuthorizationService authorizationService,
											OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
		super(authenticationManager, authorizationService, tokenGenerator);
	}

	@Override
	public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
		String refreshToken = (String) reqParameters.get(OAuth2ParameterNames.REFRESH_TOKEN);
		return new UsernamePasswordAuthenticationToken(refreshToken, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		boolean supports = RefreshAuthenticationToken.class.isAssignableFrom(authentication);
		if (log.isDebugEnabled()) {
			log.debug("supports authentication=" + authentication + " returning " + supports);
		}
		return supports;
	}

	@Override
	public void checkClient(RegisteredClient registeredClient) {
		assert registeredClient != null;
		if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}
	}

}