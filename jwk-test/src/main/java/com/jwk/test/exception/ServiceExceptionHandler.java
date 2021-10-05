package com.jwk.test.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 统一异常处理 只处理前端http接口，不处理springcloud接口
 */
@ControllerAdvice
public class ServiceExceptionHandler extends com.jwk.common.core.exception.ServiceExceptionHandler {


}
