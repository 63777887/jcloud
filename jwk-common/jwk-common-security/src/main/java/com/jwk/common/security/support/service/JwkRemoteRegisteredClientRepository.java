package com.jwk.common.security.support.service;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.upms.base.api.UpmsRemoteService;
import com.jwk.upms.base.dto.SysOauthClientDto;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.util.StringUtils;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 查询客户端相关信息实现
 * @date 2022/11/24
 */
@RequiredArgsConstructor
public class JwkRemoteRegisteredClientRepository implements RegisteredClientRepository {

	/**
	 * 刷新令牌有效期默认 30 天
	 */
	private final static int refreshTokenValiditySeconds = 60 * 60 * 24 * 30;

	/**
	 * 请求令牌有效期默认 12 小时
	 */
	private final static int accessTokenValiditySeconds = 60 * 60 * 12;

	private final UpmsRemoteService upmsRemoteService;

	private final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	/**
	 * Saves the registered client.
	 *
	 * <p>
	 * IMPORTANT: Sensitive information should be encoded externally from the
	 * implementation, e.g. {@link RegisteredClient#getClientSecret()}
	 * @param registeredClient the {@link RegisteredClient}
	 */
	@Override
	public void save(RegisteredClient registeredClient) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the registered client identified by the provided {@code id}, or
	 * {@code null} if not found.
	 * @param id the registration identifier
	 * @return the {@link RegisteredClient} if found, otherwise {@code null}
	 */
	@Override
	public RegisteredClient findById(String id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the registered client identified by the provided {@code clientId}, or
	 * {@code null} if not found.
	 * @param clientId the client identifier
	 * @return the {@link RegisteredClient} if found, otherwise {@code null}
	 */

	/**
	 * 重写原生方法远程调用
	 * @param clientId
	 * @return
	 */
	@Override
	@SneakyThrows
	public RegisteredClient findByClientId(String clientId) {

		SysOauthClientDto clientDetails = upmsRemoteService.getClientDetailsById(clientId).getData();

		if (clientDetails == null) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT, "Unknown client. clientId:" + clientId,
					ERROR_URI);
			throw new OAuth2AuthenticationException(error);
		}

		RegisteredClient.Builder builder = RegisteredClient.withId(clientDetails.getClientId())
				.clientId(clientDetails.getClientId());
		builder.clientSecret(clientDetails.getClientSecret());
		builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
		// 授权模式
		Optional.ofNullable(clientDetails.getAuthorizedGrantTypes())
				.ifPresent(grants -> StringUtils.commaDelimitedListToSet(grants)
						.forEach(s -> builder.authorizationGrantType(new AuthorizationGrantType(s))));
		// 回调地址
		Optional.ofNullable(clientDetails.getWebServerRedirectUri()).ifPresent(redirectUri -> Arrays
				.stream(redirectUri.split(StrUtil.COMMA)).filter(StrUtil::isNotBlank).forEach(builder::redirectUri));

		// scope
		Optional.ofNullable(clientDetails.getScope()).ifPresent(
				scope -> Arrays.stream(scope.split(StrUtil.COMMA)).filter(StrUtil::isNotBlank).forEach(builder::scope));

		return builder
				.tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE)
						.accessTokenTimeToLive(Duration.ofSeconds(Optional
								.ofNullable(clientDetails.getAccessTokenValidity()).orElse(accessTokenValiditySeconds)))
						.refreshTokenTimeToLive(
								Duration.ofSeconds(Optional.ofNullable(clientDetails.getRefreshTokenValidity())
										.orElse(refreshTokenValiditySeconds)))
						.build())
				.clientSettings(ClientSettings.builder()
						.requireAuthorizationConsent(!BooleanUtil.toBoolean(clientDetails.getAutoapprove())).build())
				.build();
	}

}
