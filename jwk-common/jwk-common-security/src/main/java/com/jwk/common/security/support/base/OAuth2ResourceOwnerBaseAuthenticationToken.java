package com.jwk.common.security.support.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

/**
 * @author Jiwk
 * @date 2022/11/24
 * @version 0.1.4
 * <p>
 * 自定义授权模式
 */
public abstract class OAuth2ResourceOwnerBaseAuthenticationToken extends AbstractAuthenticationToken {

	@Getter
	private final AuthorizationGrantType authorizationGrantType;

	@Getter
	private final Authentication clientPrincipal;

	@Getter
	private final Set<String> scopes;

	@Getter
	private final Map<String, Object> additionalParameters;

	public OAuth2ResourceOwnerBaseAuthenticationToken(AuthorizationGrantType authorizationGrantType,
			Authentication clientPrincipal, @Nullable Set<String> scopes,
			@Nullable Map<String, Object> additionalParameters) {
		super(Collections.emptyList());
		Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
		Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
		this.authorizationGrantType = authorizationGrantType;
		this.clientPrincipal = clientPrincipal;
		this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
		this.additionalParameters = Collections.unmodifiableMap(
				additionalParameters != null ? new HashMap<>(additionalParameters) : Collections.emptyMap());
	}

	/**
	 * 扩展模式一般不需要密码
	 */
	@Override
	public Object getCredentials() {
		return "";
	}

	/**
	 * 获取用户名
	 */
	@Override
	public Object getPrincipal() {
		return this.clientPrincipal;
	}

}
