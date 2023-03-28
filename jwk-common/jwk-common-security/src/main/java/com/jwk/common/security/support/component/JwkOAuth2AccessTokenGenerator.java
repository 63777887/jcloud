package com.jwk.common.security.support.component;

import cn.hutool.core.collection.CollUtil;
import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.core.utils.WebUtils;
import com.jwk.common.security.constants.OAuth2ErrorCodeConstant;
import com.jwk.common.security.exception.ScopeException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 自定义AccessToken生成策略
 * 重写{@link org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator}
 * @date 2022/11/14
 */
public final class JwkOAuth2AccessTokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {

	private final StringKeyGenerator accessTokenGenerator = new Base64StringKeyGenerator(
			Base64.getUrlEncoder().withoutPadding(), 96);

	private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;

	@Nullable
	@Override
	public OAuth2AccessToken generate(OAuth2TokenContext context) {

		// 根据申请code时的权限和申请token时的权限对比
		if (WebUtils.getRequest().get().getParameter(OAuth2ParameterNames.SCOPE) == null) {
			throw new ScopeException(OAuth2ErrorCodeConstant.SCOPE_IS_EMPTY);
		}
		Set<String> requestScopes = CollUtil.newHashSet(Arrays.asList(StringUtils.delimitedListToStringArray(
				WebUtils.getRequest().get().getParameter(OAuth2ParameterNames.SCOPE), ",")));
		if (!CollUtil.containsAll(context.getAuthorizedScopes(), requestScopes)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
		}

		if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) || !OAuth2TokenFormat.REFERENCE
				.equals(context.getRegisteredClient().getTokenSettings().getAccessTokenFormat())) {
			return null;
		}
		if (context.getAuthorizedScopes().contains(OidcScopes.OPENID)) {
			return new OAuth2AccessToken(TokenType.BEARER, JwkSecurityConstants.OIDC_TOKEN_DEFAULT_VALUE, Instant.now(),
					null);
		}

		String issuer = null;
		if (context.getProviderContext() != null) {
			issuer = context.getProviderContext().getIssuer();
		}
		RegisteredClient registeredClient = context.getRegisteredClient();

		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());

	// @formatter:off
    OAuth2TokenClaimsSet.Builder claimsBuilder = OAuth2TokenClaimsSet.builder();
    if (StringUtils.hasText(issuer)) {
      claimsBuilder.issuer(issuer);
    }
    claimsBuilder
        .subject(context.getPrincipal().getName())
        .audience(Collections.singletonList(registeredClient.getClientId()))
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .notBefore(issuedAt)
        .id(UUID.randomUUID().toString());
    if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
      claimsBuilder.claim(OAuth2ParameterNames.SCOPE, context.getAuthorizedScopes());
      if (context.getAuthorizedScopes().contains(OidcScopes.OPENID)) {
        claimsBuilder.expiresAt(Instant.ofEpochSecond(30 * 60));
      }
    }
    // @formatter:on

		if (this.accessTokenCustomizer != null) {
		// @formatter:off
      OAuth2TokenClaimsContext.Builder accessTokenContextBuilder = OAuth2TokenClaimsContext
          .with(claimsBuilder)
          .registeredClient(context.getRegisteredClient())
          .principal(context.getPrincipal())
          .providerContext(context.getProviderContext())
          .authorizedScopes(context.getAuthorizedScopes())
          .tokenType(context.getTokenType())
          .authorizationGrantType(context.getAuthorizationGrantType());
      if (context.getAuthorization() != null) {
        accessTokenContextBuilder.authorization(context.getAuthorization());
      }
      if (context.getAuthorizationGrant() != null) {
        accessTokenContextBuilder.authorizationGrant(context.getAuthorizationGrant());
      }
      // @formatter:on

			OAuth2TokenClaimsContext accessTokenContext = accessTokenContextBuilder.build();
			this.accessTokenCustomizer.customize(accessTokenContext);
		}

		OAuth2TokenClaimsSet accessTokenClaimsSet = claimsBuilder.build();

		return new OAuth2AccessTokenClaims(OAuth2AccessToken.TokenType.BEARER, this.accessTokenGenerator.generateKey(),
				accessTokenClaimsSet.getIssuedAt(), accessTokenClaimsSet.getExpiresAt(), context.getAuthorizedScopes(),
				accessTokenClaimsSet.getClaims());
	}

	/**
	 * Sets the {@link OAuth2TokenCustomizer} that customizes the
	 * {@link OAuth2TokenClaimsContext#getClaims() claims} for the
	 * {@link OAuth2AccessToken}.
	 * @param accessTokenCustomizer the {@link OAuth2TokenCustomizer} that customizes the
	 * claims for the {@code OAuth2AccessToken}
	 */
	public void setAccessTokenCustomizer(OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer) {
		Assert.notNull(accessTokenCustomizer, "accessTokenCustomizer cannot be null");
		this.accessTokenCustomizer = accessTokenCustomizer;
	}

	private static final class OAuth2AccessTokenClaims extends OAuth2AccessToken implements ClaimAccessor {

		private final Map<String, Object> claims;

		private OAuth2AccessTokenClaims(TokenType tokenType, String tokenValue, Instant issuedAt, Instant expiresAt,
				Set<String> scopes, Map<String, Object> claims) {
			super(tokenType, tokenValue, issuedAt, expiresAt, scopes);
			this.claims = claims;
		}

		@Override
		public Map<String, Object> getClaims() {
			return this.claims;
		}

	}

}
