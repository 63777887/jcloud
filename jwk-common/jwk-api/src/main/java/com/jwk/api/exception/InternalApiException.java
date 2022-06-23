package com.jwk.api.exception;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Fegin异常
 */
public class InternalApiException extends Exception {

  private static final long serialVersionUID = 6922970840107066104L;

  private String errorCode;

  public InternalApiException() {
    super();
  }

  public InternalApiException(String message) {
    super(message);
  }

  public InternalApiException(Throwable cause) {
    super(cause);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
