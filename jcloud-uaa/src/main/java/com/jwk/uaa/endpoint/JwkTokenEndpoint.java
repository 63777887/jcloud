package com.jwk.uaa.endpoint;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.constant.CharConstants;
import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.log.utils.SysLogUtils;
import com.jwk.common.security.annotation.Inner;
import com.jwk.common.security.constants.OAuth2Constant;
import com.jwk.common.security.constants.OAuth2ErrorCodeConstant;
import com.jwk.common.security.exception.ScopeException;
import com.jwk.common.security.support.handler.JwkAuthenticationFailureEventHandler;
import com.jwk.common.security.support.handler.JwkOAuth2AccessTokenResponseHttpMessageConverter;
import com.jwk.common.security.util.SecurityUtils;
import com.jwk.upms.base.api.UpmsRemoteService;
import com.jwk.upms.base.dto.SysOauthClientDto;
import com.jwk.upms.base.entity.SysRole;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * token控制
 * @date 2022/11/24
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class JwkTokenEndpoint {

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new JwkOAuth2AccessTokenResponseHttpMessageConverter();

	private final AuthenticationFailureHandler authenticationFailureHandler = new JwkAuthenticationFailureEventHandler();

	private final OAuth2AuthorizationService authorizationService;

	private final UpmsRemoteService upmsRemoteService;

	private Set<ScopeWithDescription> withDescription(Set<String> scopes, List<SysRole> scopeList) {
		Set<ScopeWithDescription> scopeWithDescriptions = new LinkedHashSet<>();
		for (String scope : scopes) {
			for (SysRole sysRole : scopeList) {
				if (scope.equals(sysRole.getCode())) {
					scopeWithDescriptions
							.add(new ScopeWithDescription(scope, sysRole.getRoleName(), sysRole.getRoleDesc()));
				}
			}
		}
		return scopeWithDescriptions;
	}

	/**
	 * 认证页面
	 * @param modelAndView
	 * @param error 表单登录失败处理回调的错误信息
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
		modelAndView.setViewName("login");
		modelAndView.addObject("error", error);
		return modelAndView;
	}

	@GetMapping("/confirm_access")
	public ModelAndView confirm(Principal principal, ModelAndView modelAndView,
			@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
			@RequestParam(OAuth2ParameterNames.SCOPE) String scope,
			@RequestParam(OAuth2ParameterNames.STATE) String state) {
		SysOauthClientDto clientDetails = upmsRemoteService.getClientDetailsById(clientId).getData();
		Set<String> authorizedScopes = StrUtil.isNotBlank(scope) ? new HashSet<>(StrUtil.split(scope, CharConstants.SPACE))
				: new HashSet<>(StrUtil.split(clientDetails.getScope(), CharConstants.COMMA));
		List<SysRole> scopeList = clientDetails.getScopeList();
		if (CollUtil.isEmpty(authorizedScopes) || CollUtil.isEmpty(scopeList)) {
			throw new ScopeException(OAuth2ErrorCodeConstant.SCOPE_IS_EMPTY);
		}
		modelAndView.addObject("clientId", clientId);
		modelAndView.addObject("state", state);
		modelAndView.addObject("scopes", withDescription(authorizedScopes, scopeList));
		modelAndView.addObject("principalName", principal.getName());
		modelAndView.addObject("clientName", clientDetails.getClientName());
		modelAndView.addObject("redirectUri", clientDetails.getWebServerRedirectUri());
		modelAndView.setViewName("consent");
		return modelAndView;
	}

	/**
	 * 认证页面
	 * @param modelAndView
	 * @param error 表单登录失败处理回调的错误信息
	 * @return ModelAndView
	 */
	@GetMapping("/")
	public ModelAndView defaultPage(ModelAndView modelAndView, @RequestParam(required = false) String error) {
		modelAndView.setViewName("defaultPage");
		return modelAndView;
	}

	/**
	 * 退出并删除token
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public RestResponse<Boolean> logout(
			@RequestHeader(value = OAuth2Constant.TOKEN, required = false) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			return RestResponse.success();
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		return removeToken(tokenValue);
	}

	/**
	 * 校验token
	 * @param authHeader 令牌
	 */
	@SneakyThrows
	@GetMapping("/check_token")
	public void checkToken(@RequestHeader(value = OAuth2Constant.TOKEN, required = false) String authHeader,
			HttpServletResponse response, HttpServletRequest request) {
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		//
		if (StrUtil.isBlank(authHeader)) {
			this.authenticationFailureHandler.onAuthenticationFailure(request, response,
					new InvalidBearerTokenException(OAuth2ErrorCodeConstant.TOKEN_MISSING));
		}

		String token = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		if (StrUtil.isBlank(token)) {
			httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
			this.authenticationFailureHandler.onAuthenticationFailure(request, response,
					new InvalidBearerTokenException(OAuth2ErrorCodeConstant.TOKEN_MISSING));
			return;
		}
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

		// 如果令牌不存在 返回401
		if (authorization == null || authorization.getAccessToken() == null) {
			this.authenticationFailureHandler.onAuthenticationFailure(request, response,
					new InvalidBearerTokenException(OAuth2ErrorCodeConstant.INVALID_BEARER_TOKEN));
			return;
		}

		Map<String, Object> claims = authorization.getAccessToken().getClaims();
		OAuth2AccessTokenResponse sendAccessTokenResponse = SecurityUtils.sendAccessTokenResponse(authorization,
				claims);
		this.accessTokenHttpResponseConverter.write(sendAccessTokenResponse, MediaType.APPLICATION_JSON, httpResponse);
	}

	/**
	 * 令牌管理调用
	 * @param token token
	 */
	@Inner
	@DeleteMapping("/{token}")
	public RestResponse<Boolean> removeToken(@PathVariable("token") String token) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (authorization == null) {
			return RestResponse.success();
		}

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
			return RestResponse.success();
		}
		// 清空access token
		authorizationService.remove(authorization);
		Map<String, Object> claims = authorization.getAccessToken().getClaims();
		Long userId = MapUtil.getLong(claims,JwkSecurityConstants.DETAILS_USER_ID);
		String clientId = MapUtil.getStr(claims, JwkSecurityConstants.CLIENT_ID);
		// 处理自定义退出事件，保存相关日志
		SysLogUtils.pushLogoutLog(userId,clientId);
		return RestResponse.success();
	}

	public static class ScopeWithDescription {

		public final String scope;

		public final String scopeName;

		public final String description;

		ScopeWithDescription(String scope, String scopeName, String scopeDesc) {
			this.scope = scope;
			this.scopeName = scopeName;
			this.description = scopeDesc;
		}

	}

}
