package com.jwk.common.Idgenerater.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 槽位相关配置项
 * @date 2022/11/2
 */
@Data
@ConfigurationProperties(prefix = "jwk.id.gen")
public class IdGeneraterProperties {

	/**
	 * 普通槽总数[0,16384)
	 */
	public int slotCount = 16384;

	/**
	 * 普通槽位的初始化值，产生的第一个id：初始值+1
	 */
	public long slotInitialValue = 1;

	/**
	 * 批量获取id时最大size
	 */
	public int maxSize = 1000;

	/**
	 * 分布式锁前缀名
	 */
	public String lockPrefix = "IDGEN_LOCK";

	/**
	 * IDGenKey前缀
	 */
	public String keyPrefix = "IDGEN";

}
