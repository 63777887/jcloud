package com.jwk.common.security.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 非法异常，如错误的grant异常
 */
@JsonSerialize(using = JwkAuth2ExceptionSerializer.class)
public class InvalidException extends JwkAuth2Exception {

	public InvalidException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_exception";
	}

	@Override
	public int getHttpErrorCode() {
		return 426;
	}

}
