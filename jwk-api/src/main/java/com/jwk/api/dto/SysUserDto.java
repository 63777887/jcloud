package com.jwk.api.dto;

import lombok.Data;

@Data
public class SysUserDto {

  /**
   * id
   */
  private Long id;

  /**
   * orgId
   */
  private Long orgId;

  /**
   * username
   */
  private String username;

  /**
   * password
   */
  private String password;

  /**
   * phone
   */
  private String phone;

  /**
   * email
   */
  private String email;

  /**
   * icon
   */
  private String icon;

  /**
   * enabled
   */
  private Byte status;

  /**
   * createTime
   */
  private Long createTime;

  /**
   * updateTime
   */
  private Long updateTime;


}
