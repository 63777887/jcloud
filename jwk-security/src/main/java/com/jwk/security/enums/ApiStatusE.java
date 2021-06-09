package com.jwk.security.enums;

public enum ApiStatusE {
	SC_UNAUTHORIZED(401, "用户未登陆"),
	SC_FORBIDDEN(403, "用户没有访问权限");

	ApiStatusE(Integer code, String vlaue) {
		this.code = code;
		this.value = vlaue;
	}

	public static ApiStatusE getById(Integer id){
		if(id == null){
			return null;
		}
		for(ApiStatusE e : ApiStatusE.values()){
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
		for(ApiStatusE enums : ApiStatusE.values())
		{
			if(enums.code.equals(code))
			{
				return enums.value;
			}
		}
		return "";
	}
	private Integer code;

	private String value;

	public Integer getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}