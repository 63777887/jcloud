package com.jwk.common.security.support.grant.refresh;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import com.jwk.common.security.util.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 资源所有者密码令牌授予者
 * @date 2022/6/11
 */
public class RefreshTokenGranter extends OAuth2ResourceOwnerBaseAuthenticationConverter<RefreshAuthenticationToken> {

	@Override
	public boolean support(String grantType) {
		return OAuth2ParameterNames.REFRESH_TOKEN.equals(grantType);
	}

	@Override
	public RefreshAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes,
												 Map<String, Object> additionalParameters) {
		return new RefreshAuthenticationToken(AuthorizationGrantType.REFRESH_TOKEN, clientPrincipal, requestedScopes,
				additionalParameters);
	}

	/**
	 * 校验扩展参数 密码模式密码必须不为空
	 * @param request 参数列表
	 */
	@Override
	public void checkParams(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = SecurityUtils.getParameters(request);
		// refresh (REQUIRED)
		String refreshToken = parameters.getFirst(OAuth2ParameterNames.REFRESH_TOKEN);
		if (!StringUtils.hasText(refreshToken) || parameters.get(OAuth2ParameterNames.REFRESH_TOKEN).size() != 1) {
			SecurityUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REFRESH_TOKEN,
					JwkSecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

	}

}
