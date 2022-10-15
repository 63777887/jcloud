package com.jwk.common.security.security.dto;

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
public class SysUser implements Serializable {

	private static final long serialVersionUID = 7795247620181117742L;

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
