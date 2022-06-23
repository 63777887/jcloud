package com.jwk.common.core.enums;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 统计业务类型
 */
public enum StatBusinessTypeReqE {
  /**
   * 入住人员导出
   */
  HcCheckInMember(1, "入住人员");


  StatBusinessTypeReqE(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public static StatBusinessTypeReqE getById(Integer id) {
    if (id == null) {
      return null;
    }
    for (StatBusinessTypeReqE e : StatBusinessTypeReqE.values()) {
      if (e.getId().equals(id)) {
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
  public static String getDescByCode(Integer code) {
    for (StatBusinessTypeReqE enums : StatBusinessTypeReqE.values()) {
      if (enums.id.equals(code)) {
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
