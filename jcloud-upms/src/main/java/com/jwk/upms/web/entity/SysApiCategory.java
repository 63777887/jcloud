package com.jwk.upms.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * `sys_api_category`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysApiCategory extends Model<SysApiCategory> {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * createTime
	 */
	private Long createTime;

	/**
	 * name
	 */
	private String name;

	/**
	 * sort
	 */
	private Integer sort;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
