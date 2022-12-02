package com.jwk.common.security.support.component;

import cn.hutool.core.util.StrUtil;
import com.jwk.common.security.support.properties.JwkAuthProperties;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

/**
 * @author Jiwk
 * @date 2022/11/13
 * @version 0.1.4
 * <p>
 * BearerTokenResolver的功能主要是拿到请求里的accesstoken，
 * 为什么要自定义一个类实现BearerTokenResolver呢？
 * 默认提供的DefaultBearerTokenResolver，
 * 有正则匹配的类型（"^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$"），
 * 而现在用的accesstoken的形式是Bearer clientId::username::UUID，不能匹配上，
 * 需要稍加改造，并且我们可以再添加多个类型的校验，防止伪造。
 */
public class JwkBearerTokenExtractor implements BearerTokenResolver {

	private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
			Pattern.CASE_INSENSITIVE);

	private boolean allowFormEncodedBodyParameter = false;

	private boolean allowUriQueryParameter = false;

	private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;

	private final PathMatcher pathMatcher = new AntPathMatcher();

	private final JwkAuthProperties jwkAuthProperties;

	public JwkBearerTokenExtractor(JwkAuthProperties jwkAuthProperties) {
		this.jwkAuthProperties = jwkAuthProperties;
	}

	@Override
	public String resolve(HttpServletRequest request) {

		//校验请求路径，校验是否是白名单直接返回null
		boolean match = Arrays.stream(jwkAuthProperties.getNoAuthArray())
				.anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));

		if (match) {
			return null;
		}
		//校验hearder里的accesstoken格式是否匹配，并返回token
		final String authorizationHeaderToken = resolveFromAuthorizationHeader(request);
		//校验请求方式是否是GET/POST，是就从参数中获取accesstoken，并返回token
		final String parameterToken = isParameterTokenSupportedForRequest(request) ? resolveFromRequestParameters(request) : null;
		//判断如果headertoekn不是空
		if (StrUtil.isNotBlank(authorizationHeaderToken)) {
			//如果parametertoekn不是空则抛出多个accesstoken异常
			if (StrUtil.isNotBlank(parameterToken)) {
				final BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
				throw new OAuth2AuthenticationException(error);
			}
			//如果parametertoekn是空则返回headertoekn
			return authorizationHeaderToken;
		}
		//如果parametertoekn不是空并且支持请求类型，则返回parameteken
		if (StrUtil.isNotBlank(parameterToken) && isParameterTokenEnabledForRequest(request)) {
			return parameterToken;
		}
		return null;
	}

	private String resolveFromAuthorizationHeader(HttpServletRequest request) {
		String authorization = request.getHeader(this.bearerTokenHeaderName);
		if (!StringUtils.startsWithIgnoreCase(authorization, "Bearer")) {
			return null;
		}
		Matcher matcher = authorizationPattern.matcher(authorization);
		//判断是否能匹配上
		if (!matcher.matches()) {
			BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
			throw new OAuth2AuthenticationException(error);
		}
		return matcher.group("token");
	}

	private static String resolveFromRequestParameters(HttpServletRequest request) {
		String[] values = request.getParameterValues("access_token");
		if (values == null || values.length == 0) {
			return null;
		}
		if (values.length == 1) {
			return values[0];
		}
		BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
		throw new OAuth2AuthenticationException(error);
	}

	private boolean isParameterTokenSupportedForRequest(HttpServletRequest request) {
		return (("POST".equals(request.getMethod())
				&& MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
				|| "GET".equals(request.getMethod()));
	}

	private boolean isParameterTokenEnabledForRequest(HttpServletRequest request) {
		return ((this.allowFormEncodedBodyParameter && "POST".equals(request.getMethod())
				&& MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
				|| (this.allowUriQueryParameter && "GET".equals(request.getMethod())));
	}

}
