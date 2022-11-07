package com.jwk.common.Idgenerater.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 异常处理
 * @date 2022/11/2
 */
@JsonSerialize(using = IdExceptionSerializer.class)
public class IdGeneratorException extends Exception {

  private static final long serialVersionUID = 1L;

  @Getter
  private Integer errorCode;

  public IdGeneratorException(String msg) {
    super(msg);
  }

  public IdGeneratorException(Integer errorCode,String msg) {
    super(msg);
    this.errorCode = errorCode;
  }
  public IdGeneratorException(Throwable cause) {
    super(cause);
  }

  public IdGeneratorException(String message, Throwable cause) {
    super(message, cause);
  }

  public IdGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public IdGeneratorException(IdExceptionCodeE idExceptionCodeE) {
    super(idExceptionCodeE.getErrMsg());
    this.errorCode = idExceptionCodeE.getErrCode();
  }

}
