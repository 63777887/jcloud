package com.jwk.upms.enums;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 菜单状态
 * @date 2022/10/17
 */
public enum MenuStatusE {

	/**
	 * 正常
	 */
	Normal((byte) 1, "正常"),
	/**
	 * 删除
	 */
	Delete((byte) 2, "删除");

	private final Byte id;

	private final String name;

	MenuStatusE(Byte id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuStatusE getById(Byte id) {
		if (id == null) {
			return null;
		}
		for (MenuStatusE e : MenuStatusE.values()) {
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
		for (MenuStatusE enums : MenuStatusE.values()) {
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
