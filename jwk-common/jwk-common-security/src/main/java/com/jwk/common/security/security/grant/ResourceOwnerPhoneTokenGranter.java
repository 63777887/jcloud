package com.jwk.common.security.security.grant;

import cn.hutool.core.util.StrUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 资源所有者手机令牌授予者
 */
public class ResourceOwnerPhoneTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "phone";

	private final AuthenticationManager authenticationManager;

	public ResourceOwnerPhoneTokenGranter(AuthenticationManager authenticationManager,
										  AuthorizationServerTokenServices tokenServices,
										  ClientDetailsService clientDetailsService,
										  OAuth2RequestFactory requestFactory) {
		this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	protected ResourceOwnerPhoneTokenGranter(AuthenticationManager authenticationManager,
											 AuthorizationServerTokenServices tokenServices,
											 ClientDetailsService clientDetailsService,
											 OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());

		// 手机号
		String phone = parameters.get("phone");
		// 验证码/密码
		String code = parameters.get("code");

		if (StrUtil.isBlank(phone) || StrUtil.isBlank(code)) {
			throw new InvalidGrantException("Bad credentials [ params must be has phone with code ]");
		}

		// Protect from downstream leaks of code
		parameters.remove("code");

		Authentication userAuth = new PasswordAuthenticationToken(phone, code);
		((AbstractAuthenticationToken) userAuth).setDetails(parameters);
		try {
			userAuth = authenticationManager.authenticate(userAuth);
		} catch (AccountStatusException ase) {
			//covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
			throw new InvalidGrantException(ase.getMessage());
		} catch (BadCredentialsException e) {
			// If the phone/code are wrong the spec says we should send 400/invalid grant
			throw new InvalidGrantException(e.getMessage());
		}
		if (userAuth == null || !userAuth.isAuthenticated()) {
			throw new InvalidGrantException("Could not authenticate user: " + phone);
		}

		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
		return new OAuth2Authentication(storedOAuth2Request, userAuth);
	}
}
