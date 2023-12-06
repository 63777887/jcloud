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
public class SetRoleMenuDto {

	/**
	 * roleId
	 */
	private Long roleId;

	private Integer menuType;

	/**
	 * menuIds
	 */
	private List<Long> menuIds;

}
