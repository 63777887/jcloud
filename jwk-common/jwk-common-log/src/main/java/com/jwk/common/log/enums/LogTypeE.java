package com.jwk.common.log.enums;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 错误码
 * @date 2022/10/17
 */
public enum LogTypeE {


	/**
	 * 登陆
	 */
	USER_LOGIN((byte) 1, "登陆系统"),
	USER_LOGOUT((byte) 2, "退出系统");

	private final Byte code;

	private final String msg;

	LogTypeE(Byte code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static LogTypeE getByCode(Byte code) {
		if (code == null) {
			return null;
		}
		for (LogTypeE e : LogTypeE.values()) {
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
		for (LogTypeE enums : LogTypeE.values()) {
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
