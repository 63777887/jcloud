package com.jwk.common.core.constant;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 常量
 */
public interface JwkSecurityConstants {

	/**
	 * 项目的license
	 */
	String PROJECT_LICENSE = "https://gitee.com/musi1996/jcloud.git";

	/**
	 * 内部
	 */
	String FROM_IN = "Y";

	/**
	 * 标志
	 */
	String FROM = "from";

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * 请求header
	 */
	String HEADER_FROM_IN = FROM + "=" + FROM_IN;

	/**
	 * 协议字段
	 */
	String DETAILS_LICENSE = "license";

	/**
	 * 客户端ID
	 */
	String CLIENT_ID = "clientId";

	/**
	 * 客户端模式
	 */
	String CLIENT_CREDENTIALS = "client_credentials";

	/**
	 * 用户信息
	 */
	String DETAILS_USER = "user_info";

	String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

}
