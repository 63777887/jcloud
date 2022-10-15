package com.jwk.upms.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * `sys_user`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SysUser extends Model<SysUser> {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
