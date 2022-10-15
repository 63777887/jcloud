package com.jwk.api.dto;

import lombok.Data;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 用户请求参数
 */
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
