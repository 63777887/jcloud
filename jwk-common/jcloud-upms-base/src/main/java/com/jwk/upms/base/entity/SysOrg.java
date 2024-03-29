package com.jwk.upms.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * `sys_org`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrg extends Model<SysOrg> {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * orgPid
	 */
	private Long parentId;

	/**
	 * orgName
	 */
	private String orgName;

	/**
	 * address
	 */
	private String address;

	/**
	 * phone
	 */
	private String phone;

	/**
	 * email
	 */
	private String email;

	/**
	 * orgSort
	 */
	private Integer orgSort;

	/**
	 * level
	 */
	private Integer level;

	/**
	 * status
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
