package com.jwk.common.core.utils;

import cn.hutool.core.codec.Base64;
import com.jwk.common.core.exception.CheckedException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

/**
 * Miscellaneous utilities for web applications.
 *
 * @author L.cm
 */
@Slf4j
@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

	private final String BASIC_ = "Basic ";

	private final String UNKNOWN = "unknown";

	/**
	 * 判断是否ajax请求 spring ajax 返回含有 ResponseBody 或者 RestController注解
	 * @param handlerMethod HandlerMethod
	 * @return 是否ajax请求
	 */
	public boolean isBody(HandlerMethod handlerMethod) {
		ResponseBody responseBody = ClassUtils.getAnnotation(handlerMethod, ResponseBody.class);
		return responseBody != null;
	}

	/**
	 * 读取cookie
	 * @param name cookie name
	 * @return cookie value
	 */
	public String getCookieVal(String name) {
		if (WebUtils.getRequest().isPresent()) {
			return getCookieVal(WebUtils.getRequest().get(), name);
		}
		return null;
	}

	/**
	 * 读取cookie
	 * @param request HttpServletRequest
	 * @param name cookie name
	 * @return cookie value
	 */
	public String getCookieVal(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		return cookie != null ? cookie.getValue() : null;
	}

	/**
	 * 清除 某个指定的cookie
	 * @param response HttpServletResponse
	 * @param key cookie key
	 */
	public void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 设置cookie
	 * @param response HttpServletResponse
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds maxage
	 */
	public void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	/**
	 * 获取 HttpServletRequest
	 * @return {HttpServletRequest}
	 */
	public Optional<HttpServletRequest> getRequest() {
		return Optional
				.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
	}

	/**
	 * 获取 HttpServletResponse
	 * @return {HttpServletResponse}
	 */
	public HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
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

	/**
	 * 从request 获取CLIENT_ID
	 * @return
	 */
	@SneakyThrows
	public String getClientId(ServerHttpRequest request) {
		String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		return splitClient(header)[0];
	}

	@SneakyThrows
	public String getClientId() {
		if (WebUtils.getRequest().isPresent()) {
			String header = WebUtils.getRequest().get().getHeader(HttpHeaders.AUTHORIZATION);
			return splitClient(header)[0];
		}
		return null;
	}

	@NotNull
	private static String[] splitClient(String header) {
		if (header == null || !header.startsWith(BASIC_)) {
			throw new CheckedException("请求头中client信息为空");
		}
		byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		}
		catch (IllegalArgumentException e) {
			throw new CheckedException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, StandardCharsets.UTF_8);

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new CheckedException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}

}
