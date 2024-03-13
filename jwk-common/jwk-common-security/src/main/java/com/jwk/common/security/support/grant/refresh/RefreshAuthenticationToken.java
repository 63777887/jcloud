package com.jwk.common.security.support.grant.refresh;

import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 自定义token
 * @date 2022/6/11
 */
public class RefreshAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

	public RefreshAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal,
									  Set<String> scopes, Map<String, Object> additionalParameters) {
		super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
	}

}
