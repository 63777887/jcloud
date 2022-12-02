package com.jwk.common.security.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author Jiwk
 * @date 2022/11/24
 * @version 0.1.4
 * <p>
 * ScopeException 异常信息
 */
public class ScopeException extends OAuth2AuthenticationException {

	/**
	 * Constructs a <code>ScopeException</code> with the specified message.
	 * @param msg the detail message.
	 */
	public ScopeException(String msg) {
		super(new OAuth2Error(msg), msg);
	}

	/**
	 * Constructs a {@code ScopeException} with the specified message and root cause.
	 * @param msg the detail message.
	 * @param cause root cause
	 */
	public ScopeException(String msg, Throwable cause) {
		super(new OAuth2Error(msg), cause);
	}

}
