package com.jwk.upms.enums;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 用户状态
 * @date 2022/10/17
 */
public enum ErrorCodeStatusE {

	/**
	 * 角色ID不能为空
	 */
	ROLE_ID_EMPTY("20201", "角色ID不能为空"),
	USER_ID_EMPTY("20202", "用户名不能为空"),
	PHONE_EMPTY("20203", "手机号不能为空"),
	EMAIL_EMPTY("20204", "邮箱不能为空"),
	/**
	 * 菜单ID不能为空
	 */
	MENU_ID_EMPTY("20205", "菜单ID不能为空");

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
