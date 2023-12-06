package com.jwk.upms.web.vo;

import com.jwk.upms.base.entity.SysRole;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * @date 2023/10/12
 */
@Data
public class UserVo {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 组织机构ID
	 */
	private Long orgId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 状态，1: 正常，2:删除
	 */
	private Byte status;

	/**
	 * updateTime
	 */
	private Date updateTime;

	/**
	 * 邮箱
	 */
	private List<SysRole> sysRoles;

}
