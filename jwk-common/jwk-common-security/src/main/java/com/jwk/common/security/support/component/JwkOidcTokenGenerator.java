package com.jwk.common.security.support.component;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.security.exception.ScopeException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 自定义OidcToken生成策略
 * 重写{@link org.springframework.security.oauth2.server.authorization.token.JwtGenerator}
 * @date 2022/11/14
 */
public final class JwkOidcTokenGenerator implements OAuth2TokenGenerator<Jwt> {

	private final JwtEncoder jwtEncoder;

	private OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;

	/**
	 * Constructs a {@code JwtGenerator} using the provided parameters.
	 * @param jwtEncoder the jwt encoder
	 */
	public JwkOidcTokenGenerator(JwtEncoder jwtEncoder) {
		Assert.notNull(jwtEncoder, "jwtEncoder cannot be null");
		this.jwtEncoder = jwtEncoder;
	}

	@Nullable
	@Override
	public Jwt generate(OAuth2TokenContext context) {
		if (context.getTokenType() == null
				|| (!OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue()))) {
			return null;
		}
		if (!context.getAuthorizedScopes().contains(OidcScopes.OPENID)) {
			throw new ScopeException(OAuth2ErrorCodes.INVALID_SCOPE);
		}

		String issuer = null;
		if (context.getProviderContext() != null) {
			issuer = context.getProviderContext().getIssuer();
		}
		RegisteredClient registeredClient = context.getRegisteredClient();

		Instant issuedAt = Instant.now();
		Instant expiresAt = null;
		if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
			// TODO Allow configuration for ID Token time-to-live
			expiresAt = issuedAt.plus(JwkSecurityConstants.ID_TOKEN_EXPIRE_AT, ChronoUnit.SECONDS);
		}

	// @formatter:off
    JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
    if (StringUtils.hasText(issuer)) {
      claimsBuilder.issuer(issuer);
    }
    claimsBuilder
        .subject(context.getPrincipal().getName())
        .audience(Collections.singletonList(registeredClient.getClientId()))
        .issuedAt(issuedAt)
        .expiresAt(expiresAt);
    claimsBuilder.claim(IdTokenClaimNames.AZP, registeredClient.getClientId());
    if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(context.getAuthorizationGrantType())) {
      OAuth2AuthorizationRequest authorizationRequest = context.getAuthorization().getAttribute(
          OAuth2AuthorizationRequest.class.getName());
      String nonce = (String) authorizationRequest.getAdditionalParameters()
          .get(OidcParameterNames.NONCE);
      if (StringUtils.hasText(nonce)) {
        claimsBuilder.claim(IdTokenClaimNames.NONCE, nonce);
      }
    }
    // TODO Add 'auth_time' claim
    // @formatter:on

		JwsHeader.Builder headersBuilder = JwsHeader.with(SignatureAlgorithm.RS256);

		if (this.jwtCustomizer != null) {
		// @formatter:off
      JwtEncodingContext.Builder jwtContextBuilder = JwtEncodingContext
          .with(headersBuilder, claimsBuilder)
          .registeredClient(context.getRegisteredClient())
          .principal(context.getPrincipal())
          .providerContext(context.getProviderContext())
          .authorizedScopes(context.getAuthorizedScopes())
          .tokenType(context.getTokenType())
          .authorizationGrantType(context.getAuthorizationGrantType());
      if (context.getAuthorization() != null) {
        jwtContextBuilder.authorization(context.getAuthorization());
      }
      if (context.getAuthorizationGrant() != null) {
        jwtContextBuilder.authorizationGrant(context.getAuthorizationGrant());
      }
      // @formatter:on

			JwtEncodingContext jwtContext = jwtContextBuilder.build();
			this.jwtCustomizer.customize(jwtContext);
		}

		JwsHeader headers = headersBuilder.build();
		JwtClaimsSet claims = claimsBuilder.build();

		Jwt jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(headers, claims));

		return jwt;
	}

	/**
	 * Sets the {@link OAuth2TokenCustomizer} that customizes the
	 * {@link JwtEncodingContext#getHeaders() headers} and/or
	 * {@link JwtEncodingContext#getClaims() claims} for the generated {@link Jwt}.
	 * @param jwtCustomizer the {@link OAuth2TokenCustomizer} that customizes the headers
	 * and/or claims for the generated {@code Jwt}
	 */
	public void setJwtCustomizer(OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer) {
		Assert.notNull(jwtCustomizer, "jwtCustomizer cannot be null");
		this.jwtCustomizer = jwtCustomizer;
	}

}
