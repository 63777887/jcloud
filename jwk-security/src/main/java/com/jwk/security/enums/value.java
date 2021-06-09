package com.jwk.security.enums;

public enum value {
	SC_UNAUTHORIZED(401, "用户未登陆"),
	SC_FORBIDDEN(403, "用户没有访问权限");

	value(Integer code, String vlaue) {
		this.code = code;
		this.name = vlaue;
	}

	public static value getById(Integer id){
		if(id == null){
			return null;
		}
		for(value e : value.values()){
			if(e.getCode().equals(id)){
				return e;
			}
		}
		return null;
	}

	/**
	 * 根据Code获取Value
	 *
	 * @param code 键
	 * @return 值
	 */
	public static String getDescByCode(Integer code)
	{
		for(value enums : value.values())
		{
			if(enums.code.equals(code))
			{
				return enums.name;
			}
		}
		return "";
	}
	private Integer code;

	private String name;

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}