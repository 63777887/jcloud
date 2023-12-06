package com.jwk.uaa.grant.phone;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.core.utils.WebUtils;
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
 * @version 0.1.4
 * <p>
 * 短信登录转换器
 * @date 2022/11/24
 */
public class PhoneAuthenticationGranter
		extends OAuth2ResourceOwnerBaseAuthenticationConverter<PhoneAuthenticationToken> {

	/**
	 * 是否支持此convert
	 * @param grantType 授权类型
	 * @return
	 */
	@Override
	public boolean support(String grantType) {
		return JwkOAuth2ParameterNames.PHONE.equals(grantType);
	}

	@Override
	public PhoneAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes,
			Map<String, Object> additionalParameters) {
		return new PhoneAuthenticationToken(new AuthorizationGrantType(JwkOAuth2ParameterNames.PHONE), clientPrincipal,
				requestedScopes, additionalParameters);
	}

	/**
	 * 校验扩展参数 密码模式密码必须不为空
	 * @param request 参数列表
	 */
	@Override
	public void checkParams(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = WebUtils.getParameters(request);
		// PHONE (REQUIRED)
		String phone = parameters.getFirst(JwkOAuth2ParameterNames.PHONE_PARAMETER_NAME);
		if (!StringUtils.hasText(phone) || parameters.get(JwkOAuth2ParameterNames.PHONE_PARAMETER_NAME).size() != 1) {
			SecurityUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, JwkOAuth2ParameterNames.PHONE_PARAMETER_NAME,
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
