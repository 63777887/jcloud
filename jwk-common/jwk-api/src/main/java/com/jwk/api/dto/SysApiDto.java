package com.jwk.api.dto;

import lombok.Data;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Api接口请求参数
 */
@Data
public class SysApiDto {

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
