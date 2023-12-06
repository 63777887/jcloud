package com.jwk.common.redis.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jwk.common.redis.enums.RedisExceptionCodeE;
import lombok.Getter;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 异常处理
 * @date 2022/11/2
 */
@Getter
@JsonSerialize(using = RedisExceptionSerializer.class)
public class RedisException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Integer errorCode;

	public RedisException(String msg) {
		super(msg);
	}

	public RedisException(Integer errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	public RedisException(Throwable cause) {
		super(cause);
	}

	public RedisException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RedisException(RedisExceptionCodeE redisExceptionCodeE) {
		super(redisExceptionCodeE.getErrMsg());
		this.errorCode = redisExceptionCodeE.getErrCode();
	}

}
