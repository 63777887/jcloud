package com.jwk.common.security.support.component;

import com.jwk.common.core.constant.JwkSecurityConstants;
import java.time.Instant;
import java.util.Base64;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * @author Jiwk
 * @date 2022/11/14
 * @version 0.1.4
 * <p>
 * 自定义RefreshToken生成策略
 * 重写{@link org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator}
 */
public final class JwkOAuth2RefreshTokenGenerator implements OAuth2TokenGenerator<OAuth2RefreshToken> {
	private final StringKeyGenerator refreshTokenGenerator =
			new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

	@Nullable
	@Override
	public OAuth2RefreshToken generate(OAuth2TokenContext context) {
		if (!OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType())) {
			return null;
		}
		if (context.getAuthorizedScopes().contains(OidcScopes.OPENID)){
			return new OAuth2RefreshToken(JwkSecurityConstants.OIDC_TOKEN_DEFAULT_VALUE,Instant.now());
		}
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plus(context.getRegisteredClient().getTokenSettings().getRefreshTokenTimeToLive());
		return new OAuth2RefreshToken(this.refreshTokenGenerator.generateKey(), issuedAt, expiresAt);
	}

}
