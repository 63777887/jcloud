package com.jwk.common.prometheus.exception;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 错误码
 * @date 2022/11/2
 */
public enum PrometheusExceptionCodeE {

	/**
	 * 没有找到注册服务实例
	 */
	NoRegistryServiceInstance(10301, "没有找到注册服务实例"),
	/**
	 * 节点已存在
	 */
	NodeExist(10302, "节点已存在"),
	/**
	 * 未获取到锁
	 */
	GetLockFail(10303, "未获取到锁");

	PrometheusExceptionCodeE(Integer errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public static PrometheusExceptionCodeE getByErrCode(Integer errCode) {
		if (errCode == null) {
			return null;
		}
		for (PrometheusExceptionCodeE e : PrometheusExceptionCodeE.values()) {
			if (e.getErrCode().equals(errCode)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 根据errCode获取errMsg
	 * @param errCode 键
	 * @return 值
	 */
	public static String getErrMsgByErrCode(Integer errCode) {
		for (PrometheusExceptionCodeE enums : PrometheusExceptionCodeE.values()) {
			if (enums.errCode.equals(errCode)) {
				return enums.errMsg;
			}
		}
		return "";
	}

	private Integer errCode;

	private String errMsg;

	public Integer getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

}
