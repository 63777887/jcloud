package com.jwk.upms.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * `sys_user_role`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserRole extends Model<SysUserRole> {

  private static final long serialVersionUID = 1L;

  /**
   * roleId
   */
  private Long roleId;

  /**
   * userId
   */
  private Long userId;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;


  @Override
  protected Serializable pkVal() {
    return this.id;
  }

}
