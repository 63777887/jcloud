package com.jwk.upms.enums;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 菜单类型
 * @date 2022/6/11
 */
public enum MenuTypeE {

	/**
	 * 菜单
	 */
	MENU((byte) 1, "菜单"),
	/**
	 * 按钮
	 */
	BUTTON((byte) 2, "按钮");

	private final Byte id;

	private final String name;

	MenuTypeE(Byte id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuTypeE getById(Byte id) {
		if (id == null) {
			return null;
		}
		for (MenuTypeE e : MenuTypeE.values()) {
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
		for (MenuTypeE enums : MenuTypeE.values()) {
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
