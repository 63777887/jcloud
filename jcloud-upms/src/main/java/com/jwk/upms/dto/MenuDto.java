package com.jwk.upms.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * @date 2023/10/12
 */
@Data
public class MenuDto {

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 父级ID
	 */
	@NotNull(message = "父级ID不能为空")
	private Long parentId;

	/**
	 * 菜单名
	 */
	@NotBlank(message = "菜单名不能为空")
	private String menuName;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 权限值
	 */
	private String permission;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 状态，1: 正常，2:删除
	 */
	private Byte status;

	/**
	 * 1: 展示, 2: 隐藏
	 */
	private Byte hidden = 1;

	/**
	 * 是否展示在tabs，1: 展示，2:不展示
	 */
	private Byte tab = 1;

	/**
	 * 1：菜单，2: 按钮
	 */
	private Byte type = 1;

	/**
	 * createBy
	 */
	private String createBy;

	/**
	 * updateBy
	 */
	private String updateBy;

}
