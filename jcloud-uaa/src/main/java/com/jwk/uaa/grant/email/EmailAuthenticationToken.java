package com.jwk.uaa.grant.email;

import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationToken;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 自定义邮箱token
 * @date 2022/6/11
 */
public class EmailAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

	public EmailAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal,
			Set<String> scopes, Map<String, Object> additionalParameters) {
		super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
	}

}
