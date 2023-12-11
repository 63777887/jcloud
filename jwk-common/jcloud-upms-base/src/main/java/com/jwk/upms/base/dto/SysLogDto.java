package com.jwk.upms.base.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 日志信息
 * @date 2023/12/05
 */
@Data
public class SysLogDto {

	/**
	 * 日志类型
	 */
	private Byte logType;

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
	 * 服务ID
	 */
	private String serviceId;

	/**
	 * 用户代理
	 */
	private String userAgent;

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
	private Long time;

	/**
	 * 异常信息
	 */
	private String exception;

	/**
	 * 1:成功，2:失败
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
}
