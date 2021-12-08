package com.jwk.common.core.exception;

import com.jwk.common.core.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理 只处理前端http接口，不处理springcloud接口
 */
public class ServiceExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  @ResponseBody
  RestResponse handleException(Exception e) {
    LOGGER.error(e.getMessage(), e);
    // 异常交给sentinel进行统计
//    Tracer.trace(e);
    return RestResponse.RestResponseBuilder.createFailBuilder("系统内部异常").buidler();
  }

  @ExceptionHandler(ServiceException.class)
  @ResponseBody
  RestResponse handleServiceException(ServiceException e) {
    LOGGER.warn(e.getMessage(), e);
    return RestResponse.RestResponseBuilder.createFailBuilder(e.getMessage()).buidler();
  }

  /**
   * 处理所有接口数据验证异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  RestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    String message;
    ObjectError error = e.getBindingResult().getAllErrors().get(0);
    if (error instanceof FieldError) {
      //如果是FieldError则拼接具体field
      message = ((FieldError) error).getField() + error.getDefaultMessage();
    } else {
      message = error.getDefaultMessage();
    }
    return RestResponse.RestResponseBuilder.createFailBuilder(message).buidler();
  }

  /**
   * 处理所有接口数据验证异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(BindException.class)
  @ResponseBody
  RestResponse handleBindException(BindException e) {
    String message;
    ObjectError error = e.getBindingResult().getAllErrors().get(0);
    if (error instanceof FieldError) {
      //如果是FieldError则拼接具体field
      message = ((FieldError) error).getField() + error.getDefaultMessage();
    } else {
      message = error.getDefaultMessage();
    }
    return RestResponse.RestResponseBuilder.createFailBuilder(message).buidler();
  }
}
