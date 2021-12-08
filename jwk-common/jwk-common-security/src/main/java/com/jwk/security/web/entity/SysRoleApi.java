package com.jwk.security.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * `sys_role_api`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRoleApi extends Model<SysRoleApi> {

  private static final long serialVersionUID = 1L;

  /**
   * roleId
   */
  private Long roleId;

  /**
   * apiId
   */
  private Long apiId;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;


  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
