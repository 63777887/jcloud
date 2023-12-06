package com.jwk.upms.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 日志信息
 * @date 2023/12/06
 */
@Data
public class GetSysLogDto {

	/**
	 * 日志类型
	 */
	private List<Byte> logTypeList;

	/**
	 * 日志标题
	 */
	private String logTitle;

	/**
	 * 操作IP地址
	 */
	private String remoteAddr;

	/**
	 * 请求URI
	 */
	private String requestUri;

	/**
	 * 操作方式
	 */
	private String method;

	/**
	 * 操作提交的数据
	 */
	private String params;

	/**
	 * 执行时间
	 */
	private Long mixTime;

	/**
	 * 执行时间
	 */
	private Long maxTime;

	/**
	 * 异常信息
	 */
	private String exception;

	/**
	 * 1:成功，2:失败
	 */
	private Byte status;

	/**
	 * createTime
	 */
	private Date createTime;
}
