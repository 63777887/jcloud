package com.jwk.uaa.grant.phone;

import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationToken;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 自定义短信登录token
 * @date 2022/11/24
 */
public class PhoneAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

	public PhoneAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal,
			Set<String> scopes, Map<String, Object> additionalParameters) {
		super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
	}

}
