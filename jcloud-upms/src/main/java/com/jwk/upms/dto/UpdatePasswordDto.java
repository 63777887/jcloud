package com.jwk.upms.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * @date 2023/11/22
 */
@Data
public class UpdatePasswordDto {


	/**
	 * oldPassword
	 */
	@NotBlank(message = "原始密码不能为空")
	private String oldPassword;

	/**
	 * newPassword
	 */
	@NotBlank(message = "新密码不能为空")
	private String newPassword;

	/**
	 * confirmPassword
	 */
	@NotBlank(message = "确认密码不能为空")
	private String confirmPassword;

}
