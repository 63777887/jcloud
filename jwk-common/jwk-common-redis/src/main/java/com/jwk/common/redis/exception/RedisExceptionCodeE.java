package com.jwk.common.redis.exception;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 错误码
 * @date 2022/11/2
 */
public enum RedisExceptionCodeE {

	/**
	 * 当前锁已被他人获取
	 */
	LockIsExist(10101, "当前锁已被他人获取"),
	/**
	 * 获取锁失败，产生异常
	 */
	GetLockError(10102, "获取锁失败"),
	/**
	 * 未获取到锁
	 */
	GetLockFail(10103, "未获取到锁");

	RedisExceptionCodeE(Integer errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public static RedisExceptionCodeE getByErrCode(Integer errCode) {
		if (errCode == null) {
			return null;
		}
		for (RedisExceptionCodeE e : RedisExceptionCodeE.values()) {
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
		for (RedisExceptionCodeE enums : RedisExceptionCodeE.values()) {
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
