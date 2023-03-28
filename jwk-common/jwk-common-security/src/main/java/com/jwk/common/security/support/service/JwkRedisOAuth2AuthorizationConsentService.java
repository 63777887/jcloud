package com.jwk.common.security.support.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 自定义redis 授权同意
 * @date 2022/12/2
 */
@RequiredArgsConstructor
public class JwkRedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	private final static Long TIMEOUT = 10L;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");

		redisTemplate.opsForValue().set(buildKey(authorizationConsent), authorizationConsent, TIMEOUT,
				TimeUnit.MINUTES);

	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		redisTemplate.delete(buildKey(authorizationConsent));
	}

	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		return (OAuth2AuthorizationConsent) redisTemplate.opsForValue()
				.get(buildKey(registeredClientId, principalName));
	}

	private String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
		return buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}

	private String buildKey(String registeredClientId, String principalName) {
		return "code:consent:" + registeredClientId + ":" + principalName;
	}

}
