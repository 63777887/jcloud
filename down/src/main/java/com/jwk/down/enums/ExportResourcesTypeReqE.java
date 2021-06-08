package com.jwk.down.enums;

/**
 * 导出类型
 */
public enum ExportResourcesTypeReqE {
    /**
     * 资源清单
     */
    resourceList(1, "导出资源清单");





    ExportResourcesTypeReqE(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ExportResourcesTypeReqE getById(Integer id){
        if(id == null){
            return null;
        }
        for(ExportResourcesTypeReqE e : ExportResourcesTypeReqE.values()){
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
        for(ExportResourcesTypeReqE enums : ExportResourcesTypeReqE.values())
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
