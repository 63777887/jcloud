package com.jwk.common.security.support.grant.password;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import com.jwk.common.security.util.SecurityUtils;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 资源所有者密码令牌授予者
 */
public class PasswordTokenGranter
		extends OAuth2ResourceOwnerBaseAuthenticationConverter<PasswordAuthenticationToken> {

	private final String GRANT_TYPE = "password";

	@Override
	public boolean support(String grantType) {
		return GRANT_TYPE.equals(grantType);
	}

	@Override
	public PasswordAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes,
			Map<String, Object> additionalParameters) {
		return new PasswordAuthenticationToken(AuthorizationGrantType.PASSWORD, clientPrincipal, requestedScopes,
				additionalParameters);
	}

	/**
	 * 校验扩展参数 密码模式密码必须不为空
	 * @param request 参数列表
	 */
	@Override
	public void checkParams(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = SecurityUtils.getParameters(request);
		// username (REQUIRED)
		String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
		if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
			SecurityUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.USERNAME,
					JwkSecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// password (REQUIRED)
		String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
		if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
			SecurityUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.PASSWORD,
					JwkSecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}
	}

}
