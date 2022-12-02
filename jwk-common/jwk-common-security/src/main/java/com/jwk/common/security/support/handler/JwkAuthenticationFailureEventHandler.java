package com.jwk.common.security.support.handler;

import com.jwk.common.core.model.RestResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author Jiwk
 * @date 2022/11/14
 * @version 0.1.4
 * <p>
 * 登陆失败拦截器
 */
@Slf4j
public class JwkAuthenticationFailureEventHandler implements AuthenticationFailureHandler {

	private final MappingJackson2HttpMessageConverter errorHttpResponseConverter = new MappingJackson2HttpMessageConverter();

	/**
	 * Called when an authentication attempt fails.
	 * @param request the request during which the authentication attempt occurred.
	 * @param response the response.
	 * @param exception the exception which was thrown to reject the authentication
	 * request.
	 */
	@Override
	@SneakyThrows
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		String username = request.getParameter(OAuth2ParameterNames.USERNAME);

		if (log.isDebugEnabled()) {
			log.debug("用户：{} 登录失败，异常：{}", username, exception.getMessage());
		}
		// 写出错误信息
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
		if (exception instanceof OAuth2AuthenticationException){
		this.errorHttpResponseConverter.write(RestResponse.error(((OAuth2AuthenticationException)exception).getError().getErrorCode(),((OAuth2AuthenticationException)exception).getError().getDescription()), MediaType.APPLICATION_JSON,
				httpResponse);
		}else {
			this.errorHttpResponseConverter.write(RestResponse.error(exception.getMessage()), MediaType.APPLICATION_JSON,
					httpResponse);
		}
	}

}
