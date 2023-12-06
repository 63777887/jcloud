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
 * `sys_api`
 * </p>
 *
 * @author jiwk
 * @since 2023-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysApi extends Model<SysApi> {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 接口描述
	 */
	private String apiDesc;

	/**
	 * 接口URL
	 */
	private String url;

	/**
	 * 状态，1: 正常，2:删除
	 */
	private Byte status;

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
