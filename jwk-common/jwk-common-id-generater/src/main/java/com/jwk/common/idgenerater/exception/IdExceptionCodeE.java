package com.jwk.common.idgenerater.exception;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 错误码
 * @date 2022/11/2
 */
public enum IdExceptionCodeE {

	/**
	 * 不支持的type
	 */
	IllegalType(-1, "不支持的type"),
	/**
	 * 没有合法id提供
	 */
	NoUserFulId(-2, "没有合法id提供"),
	/**
	 * 批量获取id时，size超过最大限制
	 */
	SizeLimit(-3, "批量获取id时，size超过最大限制"), NoServiceImpl(-4, "没有对应的实现类");

	private final Integer errCode;

	private final String errMsg;

	IdExceptionCodeE(Integer errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public static IdExceptionCodeE getByErrCode(Integer errCode) {
		if (errCode == null) {
			return null;
		}
		for (IdExceptionCodeE e : IdExceptionCodeE.values()) {
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
		for (IdExceptionCodeE enums : IdExceptionCodeE.values()) {
			if (enums.errCode.equals(errCode)) {
				return enums.errMsg;
			}
		}
		return "";
	}

	public Integer getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

}
