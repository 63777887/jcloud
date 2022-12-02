package com.jwk.common.security.support.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwk.common.core.constant.ResponseConstants;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.constant.JwkSecurityConstants;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Jiwk
 * @date 2022/11/13
 * @version 0.1.3
 * <p>
 * 客户端认证异常处理 AuthenticationException 不同细化异常处理
 */
@RequiredArgsConstructor
public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		response.setCharacterEncoding(JwkSecurityConstants.UTF8);
		response.setContentType(MediaType.APPLICATION_CBOR_VALUE);
		RestResponse<String> result = new RestResponse<>();
		result.setCode(ResponseConstants.ERROR_CODE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		if (authException != null) {
			result.setMsg("error");
			result.setData(authException.getMessage());
		}

		// 针对令牌过期返回特殊的 424
		if (authException instanceof InvalidBearerTokenException
				|| authException instanceof InsufficientAuthenticationException) {
			response.setStatus(HttpStatus.FAILED_DEPENDENCY.value());
			result.setMsg(authException.getMessage());
		}
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(result));
	}

}
