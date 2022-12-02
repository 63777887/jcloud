package com.jwk.common.security.support.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Jiwk
 * @date 2022/11/14
 * @version 0.1.4
 * <p>
 * 表单登录失败拦截器
 */
@Slf4j
public class FormAuthenticationFailureHandler implements AuthenticationFailureHandler {

	/**
	 * Called when an authentication attempt fails.
	 * @param request the request during which the authentication attempt occurred.
	 * @param response the response.
	 * @param exception the exception which was thrown to reject the authentication
	 */
	@Override
	@SneakyThrows
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		if (log.isDebugEnabled()) {
			log.debug("表单登录失败:{}", exception.getLocalizedMessage());
		}
		String url = HttpUtil.encodeParams(String.format("/token/login?error=%s", exception.getMessage()),
				CharsetUtil.CHARSET_UTF_8);
		Objects.requireNonNull(
				((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
						.getResponse())
				.sendRedirect(url);
	}

}
