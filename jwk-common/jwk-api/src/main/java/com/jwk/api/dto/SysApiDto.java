package com.jwk.api.dto;

import lombok.Data;

@Data
public class SysApiDto {

  /**
   * id
   */
  private Long id;

  /**
   * apiPid
   */
  private Long parentId;

  /**
   * apiName
   */
  private String apiDesc;

  /**
   * url
   */
  private String url;

  /**
   * level
   */
  private Integer level;

  /**
   * status
   */
  private Byte status;

  /**
   * createBy
   */
  private String createBy;

  /**
   * createTime
   */
  private Long createTime;

  /**
   * updateBy
   */
  private String updateBy;

  /**
   * updateTime
   */
  private Long updateTime;

  private Long categoryId;

}
