package com.jwk.upms.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * @date 2023/10/12
 */
@Data
public class UserDto {

	/**
	 * id
	 */
	@NotNull
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
	 * nickname
	 */
	private String nickname;

	/**
	 * status
	 */
	private Integer status;

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
	 * 头像
	 */
	private String icon;

	private List<Long> roles;

}
