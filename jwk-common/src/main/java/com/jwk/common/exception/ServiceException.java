package com.jwk.common.exception;

/**
 * 业务异常
 */
public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = 6922970840107066104L;

  private String errorCode;

  public ServiceException() {
    super();
  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
