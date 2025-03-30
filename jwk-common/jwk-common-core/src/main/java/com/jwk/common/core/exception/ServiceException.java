package com.jwk.common.core.exception;

import com.jwk.common.core.constant.ResponseConstants;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 业务异常
 * @date 2022/6/11
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

	public static void throwError() {
		throw new ServiceException();
	}

	public static ServiceException buildInvalidParamsError(String message) {
		return new ServiceException(ResponseConstants.INVALID_PARAMS_ERROR_CODE, message);
	}

	public static void throwInvalidParamsError(String message) {
		throw buildInvalidParamsError(message);
	}

	public static void throwError(String message) {
		throw new ServiceException(message);
	}

	public static void throwError(Throwable cause) {
		throw new ServiceException(cause);
	}

	public static void throwError(String errorCode, String msg) {
		throw new ServiceException(errorCode, msg);
	}

}
