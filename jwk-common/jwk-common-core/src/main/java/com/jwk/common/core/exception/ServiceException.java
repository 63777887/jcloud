package com.jwk.common.core.exception;

import com.jwk.common.core.constant.ResponseConstants;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 业务异常
 *
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 6922970840107066104L;

	private String errorCode;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
		setErrorCode(ResponseConstants.ERROR_CODE);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String errorCode, String msg) {
		super(msg);
		setErrorCode(errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
