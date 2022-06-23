package com.jwk.security.enums;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 权限枚举
 */
public enum ApiStatusE {
  /**
   * 401: 用户未登陆或登录已过期
   */
  SC_UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "用户未登陆或登录已过期"),
  /**
   * 403: 用户没有访问权限
   */
  SC_FORBIDDEN(HttpServletResponse.SC_FORBIDDEN, "用户没有访问权限");

  ApiStatusE(Integer code, String vlaue) {
    this.code = code;
    this.value = vlaue;
  }

  public static ApiStatusE getById(Integer id) {
    if (id == null) {
      return null;
    }
    for (ApiStatusE e : ApiStatusE.values()) {
      if (e.getCode().equals(id)) {
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
    for (ApiStatusE enums : ApiStatusE.values()) {
      if (enums.code.equals(code)) {
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