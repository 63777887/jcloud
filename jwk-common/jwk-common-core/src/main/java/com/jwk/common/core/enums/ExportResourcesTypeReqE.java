package com.jwk.common.core.enums;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 导出类型
 * @date 2022/6/11
 */
public enum ExportResourcesTypeReqE {

	/**
	 * 资源清单
	 */
	resourceList(1, "导出资源清单");

	private final Integer id;

	private final String name;

	ExportResourcesTypeReqE(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static ExportResourcesTypeReqE getById(Integer id) {
		if (id == null) {
			return null;
		}
		for (ExportResourcesTypeReqE e : ExportResourcesTypeReqE.values()) {
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
	public static String getDescByCode(Integer code) {
		for (ExportResourcesTypeReqE enums : ExportResourcesTypeReqE.values()) {
			if (enums.id.equals(code)) {
				return enums.name;
			}
		}
		return "";
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
