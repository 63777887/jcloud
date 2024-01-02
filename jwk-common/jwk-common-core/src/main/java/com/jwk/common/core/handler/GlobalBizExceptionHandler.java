package com.jwk.common.core.handler;

import com.jwk.common.core.enums.ErrorCodeStatusE;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.common.core.model.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 全局异常拦截
 * @date 2023/10/17
 */
@Slf4j
@Order(10000)
@RestControllerAdvice
@Configuration
public class GlobalBizExceptionHandler {

	/**
	 * 业务异常
	 * @param exception
	 * @return RestResponse
	 */
	@ExceptionHandler({ ServiceException.class })
	public RestResponse serviceExceptionHandler(ServiceException exception) {
		log.warn("业务异常,ex = {}", exception.getMessage());
		return RestResponse.error(exception.getErrorCode(), exception.getMessage());
	}

	/**
	 * 全局异常.
	 * @param e the e
	 * @return RestResponse
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestResponse handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);
		return RestResponse.error(e.getLocalizedMessage());
	}

	/**
	 * 404.
	 * @param e the e
	 * @return RestResponse
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public RestResponse handleNotFoundException(NoHandlerFoundException e) {
		log.error("404异常信息 ex={}", e.getMessage(), e);
		return RestResponse.error(e.getLocalizedMessage());
	}

	/**
	 * 非法请求方式.
	 * @param e the e
	 * @return RestResponse
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public RestResponse handleNotFoundException(HttpRequestMethodNotSupportedException e) {
		log.error("非法请求方式异常信息 ex={}", e.getMessage(), e);
		return RestResponse.error(ErrorCodeStatusE.REQUEST_METHOD_NOT_SUPPORTED.getCode(),
				ErrorCodeStatusE.REQUEST_METHOD_NOT_SUPPORTED.getMsg(), e.getMessage());
	}

	/**
	 * 处理业务校验过程中碰到的非法参数异常 该异常基本由{@link Assert}抛出
	 * @param exception 参数校验异常
	 * @return API返回结果对象包装后的错误输出结果
	 * @see Assert#hasLength(String, String)
	 * @see Assert#hasText(String, String)
	 * @see Assert#isTrue(boolean, String)
	 * @see Assert#isNull(Object, String)
	 * @see Assert#notNull(Object, String)
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.OK)
	public RestResponse handleIllegalArgumentException(IllegalArgumentException exception) {
		log.error("非法参数,ex = {}", exception.getMessage(), exception);
		return RestResponse.error(exception.getMessage());
	}

	/**
	 * AccessDeniedException
	 * @param e the e
	 * @return RestResponse
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public RestResponse handleAccessDeniedException(AccessDeniedException e) {
		log.warn("拒绝授权异常信息 ex={}", e.getMessage());
		return RestResponse.error(e.getLocalizedMessage());
	}

	/**
	 * validation Exception
	 * @param exception
	 * @return RestResponse
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestResponse handleBodyValidException(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return RestResponse
				.error(String.format("%s %s", fieldErrors.get(0).getField(), fieldErrors.get(0).getDefaultMessage()));
	}

	/**
	 * validation Exception (以form-data形式传参)
	 * @param exception
	 * @return RestResponse
	 */
	@ExceptionHandler({ BindException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestResponse bindExceptionHandler(BindException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return RestResponse.error(fieldErrors.get(0).getDefaultMessage());
	}

}
