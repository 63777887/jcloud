package com.jwk.common.security.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * `sys_api`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysApi implements Serializable {

	private static final long serialVersionUID = 8299027290985406921L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * apiPid
	 */
	private Long parentId;

	/**
	 * apiName
	 */
	private String apiDesc;

	/**
	 * url
	 */
	private String url;

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
	private Long createTime;

	/**
	 * updateBy
	 */
	private String updateBy;

	/**
	 * updateTime
	 */
	private Long updateTime;

	private Long categoryId;

}
