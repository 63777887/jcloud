package com.jwk.common.log.enums;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 日志类型
 * @date 2022/10/17
 */
public enum LogStatusE {


	/**
	 * 登陆
	 */
	SUCCESS_LOG((byte) 1, "成功日志"),
	FAIL_LOG((byte) 2, "失败日志");

	private final Byte code;

	private final String msg;

	LogStatusE(Byte code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static LogStatusE getByCode(Byte code) {
		if (code == null) {
			return null;
		}
		for (LogStatusE e : LogStatusE.values()) {
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
	public static String getDescByCode(Byte code) {
		for (LogStatusE enums : LogStatusE.values()) {
			if (enums.code.equals(code)) {
				return enums.msg;
			}
		}
		return "";
	}

	public Byte getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
