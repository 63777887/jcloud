package com.jwk.upms.enums;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 用户状态
 * @date 2022/10/17
 */
public enum UserStatusE {

	/**
	 * 正常
	 */
	Normal((byte) 1, "正常"),
	/**
	 * 停用
	 */
	Stop((byte) 2, "停用");

	private final Byte id;

	private final String name;

	UserStatusE(Byte id, String name) {
		this.id = id;
		this.name = name;
	}

	public static UserStatusE getById(Byte id) {
		if (id == null) {
			return null;
		}
		for (UserStatusE e : UserStatusE.values()) {
			if (e.getId().equals(id)) {
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
		for (UserStatusE enums : UserStatusE.values()) {
			if (enums.id.equals(code)) {
				return enums.name;
			}
		}
		return "";
	}

	public Byte getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
