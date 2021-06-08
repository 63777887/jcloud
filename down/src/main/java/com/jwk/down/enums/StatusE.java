package com.jwk.down.enums;

public enum StatusE {
	Delete(0, "删除"),
	Normal(1, "正常");

	StatusE(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static StatusE getById(Integer id){
		if(id == null){
			return null;
		}
		for(StatusE e : StatusE.values()){
			if(e.getId().equals(id)){
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
		for(StatusE enums : StatusE.values())
		{
			if(enums.id.equals(code))
			{
				return enums.name;
			}
		}
		return "";
	}
	private Integer id;

	private String name;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}