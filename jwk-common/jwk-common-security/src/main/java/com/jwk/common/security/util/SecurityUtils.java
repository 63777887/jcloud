package com.jwk.common.security.util;

import cn.hutool.core.map.MapUtil;
import com.jwk.common.security.dto.AdminUserDetails;
import com.jwk.upms.base.entity.SysUser;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 安全工具类
 */
@UtilityClass
public class SecurityUtils {

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	private SysUser getUser(Authentication authentication) {
		Object principal = authentication.getDetails();
		if (principal instanceof SysUser) {
			return (SysUser) principal;
		}
		if (principal instanceof AdminUserDetails) {
			return ((AdminUserDetails) principal).getSysUser();
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public SysUser getUser() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		return getUser(authentication);
	}

	public MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
		parameterMap.forEach((key, values) -> {
			if (values.length > 0) {
				for (String value : values) {
					parameters.add(key, value);
				}
			}
		});
		return parameters;
	}

	public boolean matchesPkceTokenRequest(HttpServletRequest request) {
		return AuthorizationGrantType.AUTHORIZATION_CODE.getValue()
				.equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE))
				&& request.getParameter(OAuth2ParameterNames.CODE) != null
				&& request.getParameter(PkceParameterNames.CODE_VERIFIER) != null;
	}

	public void throwError(String errorCode, String parameterName, String errorUri) {
		OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
		throw new OAuth2AuthenticationException(error);
	}

	/**
	 * 格式化输出token 信息
	 * @param authentication 用户认证信息
	 * @param claims 扩展信息
	 * @return
	 * @throws IOException
	 */
	public OAuth2AccessTokenResponse sendAccessTokenResponse(OAuth2Authorization authentication,
			Map<String, Object> claims) {

		OAuth2AccessToken accessToken = authentication.getAccessToken().getToken();
		OAuth2RefreshToken refreshToken = authentication.getRefreshToken().getToken();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
				.tokenType(accessToken.getTokenType()).scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}
		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}

		if (MapUtil.isNotEmpty(claims)) {
			builder.additionalParameters(claims);
		}
		return builder.build();
	}

}
