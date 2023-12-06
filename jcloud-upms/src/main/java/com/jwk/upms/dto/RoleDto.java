package com.jwk.upms.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * @date 2023/10/12
 */
@Data
public class RoleDto {

	/**
	 * id
	 */
	private Long id;

	/**
	 * roleName
	 */
	@NotBlank(message = "角色名不能为空")
	private String roleName;

	/**
	 * code
	 */
	@NotBlank(message = "角色标识不能为空")
	private String code;

	/**
	 * roleDesc
	 */
	private String roleDesc;

}
