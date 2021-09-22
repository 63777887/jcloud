package com.jwk.security.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


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
