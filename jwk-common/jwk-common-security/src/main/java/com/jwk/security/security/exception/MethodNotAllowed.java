package com.jwk.security.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;


/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 方法不支持异常
 */
@JsonSerialize(using = JwkAuth2ExceptionSerializer.class)
public class MethodNotAllowed extends JwkAuth2Exception {

	public MethodNotAllowed(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "method_not_allowed";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.METHOD_NOT_ALLOWED.value();
	}

}
