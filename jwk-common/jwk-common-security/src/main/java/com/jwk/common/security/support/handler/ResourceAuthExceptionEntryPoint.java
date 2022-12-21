package com.jwk.common.security.support.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwk.common.core.constant.ResponseConstants;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.security.constants.OAuth2ErrorCodeConstant;
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
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

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
			result.setMsg(OAuth2ErrorCodeConstant.INVALID_BEARER_TOKEN);
		}
		// 不带Form头信息认证失败时，删除请求缓存，否则会一直认证失败（可能会引起HTTP会话泛洪攻击）
		// 可以不删除请求缓存，通过网关拒绝Form认证方式
		new HttpSessionRequestCache().removeRequest(request,response);
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(result));
	}

}
