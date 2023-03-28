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
 * @version 0.1.0
 * <p>
 * Fegin接口返回值
 * @date 2022/6/11
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class InnerResponse<T> implements Serializable {

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

	public static <T> InnerResponse<T> success() {
		return restResult(ResponseConstants.SUCCESS_CODE, "", null);
	}

	public static <T> InnerResponse<T> success(T data) {
		return restResult(ResponseConstants.SUCCESS_CODE, null, data);
	}

	public static <T> InnerResponse<T> success(String msg, T data) {
		return restResult(ResponseConstants.SUCCESS_CODE, msg, data);
	}

	public static <T> InnerResponse<T> error() {
		return restResult(ResponseConstants.ERROR_CODE, null, null);
	}

	public static <T> InnerResponse<T> error(T data) {
		return restResult(ResponseConstants.ERROR_CODE, null, data);
	}

	public static <T> InnerResponse<T> error(String msg, T data) {
		return restResult(ResponseConstants.ERROR_CODE, msg, data);
	}

	private static <T> InnerResponse<T> restResult(String code, String msg, T data) {
		InnerResponse<T> apiResult = new InnerResponse<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

}
