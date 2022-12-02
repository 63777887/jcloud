package com.jwk.uaa.grant.email;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import com.jwk.common.security.util.SecurityUtils;
import com.jwk.uaa.constant.JwkOAuth2ParameterNames;
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
 * 资源所有者邮箱令牌授予者
 */
public class EmailAuthenticationGranter
		extends OAuth2ResourceOwnerBaseAuthenticationConverter<EmailAuthenticationToken> {


	@Override
	public boolean support(String grantType) {
		return JwkOAuth2ParameterNames.EMAIL.equals(grantType);
	}

	@Override
	public EmailAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes,
			Map<String, Object> additionalParameters) {
		return new EmailAuthenticationToken(new AuthorizationGrantType(JwkOAuth2ParameterNames.EMAIL), clientPrincipal, requestedScopes,
				additionalParameters);
	}

	/**
	 * 校验扩展参数 密码模式密码必须不为空
	 * @param request 参数列表
	 */
	@Override
	public void checkParams(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = SecurityUtils.getParameters(request);
		// email (REQUIRED)
		String username = parameters.getFirst(JwkOAuth2ParameterNames.EMAIL_PARAMETER_NAME);
		if (!StringUtils.hasText(username) || parameters.get(JwkOAuth2ParameterNames.EMAIL_PARAMETER_NAME).size() != 1) {
			SecurityUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, JwkOAuth2ParameterNames.EMAIL_PARAMETER_NAME,
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
