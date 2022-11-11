package com.jwk.common.prometheus.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 异常处理
 * @date 2022/11/2
 */
@JsonSerialize(using = PrometheusExceptionSerializer.class)
public class PrometheusException extends Exception {

  private static final long serialVersionUID = 1L;

  @Getter
  private Integer errorCode;

  public PrometheusException(String msg) {
    super(msg);
  }

  public PrometheusException(Integer errorCode,String msg) {
    super(msg);
    this.errorCode = errorCode;
  }
  public PrometheusException(Throwable cause) {
    super(cause);
  }

  public PrometheusException(String message, Throwable cause) {
    super(message, cause);
  }

  public PrometheusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public PrometheusException(PrometheusExceptionCodeE prometheusExceptionCodeE) {
    super(prometheusExceptionCodeE.getErrMsg());
    this.errorCode = prometheusExceptionCodeE.getErrCode();
  }

}
