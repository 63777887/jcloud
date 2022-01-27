package com.jwk.upms.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * `sys_api`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysApi extends Model<SysApi> {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @TableId(value = "id", type = IdType.AUTO)
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


  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
