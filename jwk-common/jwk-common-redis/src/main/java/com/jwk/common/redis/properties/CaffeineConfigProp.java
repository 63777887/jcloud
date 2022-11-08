package com.jwk.common.redis.properties;

import com.jwk.common.redis.enums.CaffeineStrength;
import java.time.Duration;
import lombok.Data;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.3
 * <p>
 * Caffeine 相关配置
 */
@Data
public class CaffeineConfigProp {

	/**
	 * 初始化大小
	 */
	private int initialCapacity;

	/**
	 * 最大缓存对象个数，超过此数量时之前放入的缓存将失效
	 */
	private long maximumSize;

	/**
	 * key 强度
	 */
	private CaffeineStrength keyStrength;

	/**
	 * value 强度
	 */
	private CaffeineStrength valueStrength;

}
