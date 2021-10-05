package com.jwk.security.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2Exception
 */
@JsonSerialize(using = JwkAuth2ExceptionSerializer.class)
public class JwkAuth2Exception extends OAuth2Exception {

	@Getter
	private String errorCode;

	public JwkAuth2Exception(String msg) {
		super(msg);
	}

	public JwkAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}

}
