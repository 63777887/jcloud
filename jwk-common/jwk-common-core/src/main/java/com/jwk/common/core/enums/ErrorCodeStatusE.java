package com.jwk.common.core.enums;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 错误码
 * @date 2022/10/17
 */
public enum ErrorCodeStatusE {


	/**
	 * 请求方式不支持
	 */
	REQUEST_METHOD_NOT_SUPPORTED("10101", "请求方式不支持");

	private final String code;

	private final String msg;

	ErrorCodeStatusE(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ErrorCodeStatusE getByCode(String code) {
		if (code == null) {
			return null;
		}
		for (ErrorCodeStatusE e : ErrorCodeStatusE.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 根据Code获取Value
	 * @param code 键
	 * @return 值
	 */
	public static String getDescByCode(String code) {
		for (ErrorCodeStatusE enums : ErrorCodeStatusE.values()) {
			if (enums.code.equals(code)) {
				return enums.msg;
			}
		}
		return "";
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
