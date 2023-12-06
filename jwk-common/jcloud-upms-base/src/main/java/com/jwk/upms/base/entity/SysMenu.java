package com.jwk.upms.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * `sys_menu`
 * </p>
 *
 * @author jiwk
 * @since 2023-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenu extends Model<SysMenu> {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 父级ID
	 */
	private Long parentId;

	/**
	 * 菜单名
	 */
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
	private Byte hidden;

	/**
	 * 是否展示在tabs，1: 展示，2:不展示
	 */
	private Byte tab;

	/**
	 * 1：菜单，2: 按钮
	 */
	private Byte type;

	/**
	 * createBy
	 */
	private String createBy;

	/**
	 * createTime
	 */
	private Date createTime;

	/**
	 * updateBy
	 */
	private String updateBy;

	/**
	 * updateTime
	 */
	private Date updateTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
