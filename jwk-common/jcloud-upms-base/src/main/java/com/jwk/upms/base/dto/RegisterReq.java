package com.jwk.upms.base.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 用户参数
 * @date 2022/6/11
 */
@Data
public class RegisterReq implements Serializable {

	private static final long serialVersionUID = -9137840398633566755L;

	/**
	 * orgId
	 */
	@NotNull
	private Long orgId;

	/**
	 * username
	 */
	@NotBlank(message = "用户名不能为空")
	private String username;

	/**
	 * nickname
	 */
	@NotBlank(message = "用户昵称不能为空")
	private String nickname;

	/**
	 * status
	 */
	private Byte status = 1;

	/**
	 * password
	 */
	@NotBlank(message = "密码不能为空")
	private String password;

	/**
	 * phone
	 */
	@NotBlank(message = "手机号不能为空")
	private String phone;

	/**
	 * email
	 */
	@NotBlank(message = "邮箱不能为空")
	private String email;

	List<Long> roles;

}
