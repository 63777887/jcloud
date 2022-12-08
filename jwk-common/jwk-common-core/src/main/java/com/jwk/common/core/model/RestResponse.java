package com.jwk.common.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jwk.common.core.constant.ResponseConstants;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 接口返回值
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RestResponse<T> implements Serializable {

	private static final long serialVersionUID = 1078301560542522627L;

	@JsonProperty("code")
	@Setter
	@Getter
	private String code;

	@JsonProperty("msg")
	@Setter
	@Getter
	private String msg;

	@JsonProperty("data")
	@Setter
	@Getter
	private T data;

	public static <T> RestResponse<T> success() {
		return restResult(ResponseConstants.SUCCESS_CODE, ResponseConstants.SUCCESS_MSG, null);
	}

	public static <T> RestResponse<T> success(T data) {
		return restResult(ResponseConstants.SUCCESS_CODE, ResponseConstants.SUCCESS_MSG, data);
	}

	public static <T> RestResponse<T> success(String msg, T data) {
		return restResult(ResponseConstants.SUCCESS_CODE, msg, data);
	}

	public static <T> RestResponse<T> error() {
		return restResult(ResponseConstants.ERROR_CODE, ResponseConstants.ERROR_MSG, null);
	}

	public static <T> RestResponse<T> error(T data) {
		return restResult(ResponseConstants.ERROR_CODE, ResponseConstants.ERROR_MSG, data);
	}

	public static <T> RestResponse<T> error(String msg, T data) {
		return restResult(ResponseConstants.ERROR_CODE, msg, data);
	}

	public static <T> RestResponse<T> error(String code, String msg, T data) {
		return restResult(code, msg, data);
	}

	private static <T> RestResponse<T> restResult(String code, String msg, T data) {
		RestResponse<T> apiResult = new RestResponse<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

}
