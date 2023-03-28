package com.jwk.common.redis.enums;

import com.jwk.common.redis.exception.RedisExceptionCodeE;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 缓存类型
 * @date 2022/11/8
 */
public enum CacheType {

	/**
	 * Redis缓存二级
	 */
	RedisCache("2", "RedisCache"),
	/**
	 * Caffeine一级缓存
	 */
	RedisCaffeineCache("1", "RedisCaffeineCache");

	private final String cacheType;

	private final String cacheName;

	CacheType(String cacheType, String cacheName) {
		this.cacheType = cacheType;
		this.cacheName = cacheName;
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
	public static String getErrMsgByErrCode(String errCode) {
		for (CacheType enums : CacheType.values()) {
			if (enums.cacheType.equals(errCode)) {
				return enums.cacheName;
			}
		}
		return "";
	}

	public String getCacheType() {
		return cacheType;
	}

	public String getCacheName() {
		return cacheName;
	}

}
