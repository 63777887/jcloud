package com.jwk.common.redis.properties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.1
 * <p>
 * Redis 相关配置
 */
@Data
public class RedisConfigProp {

	/**
	 * 全局过期时间，默认不过期
	 */
	private Duration defaultExpiration = Duration.ZERO;

	/**
	 * 全局空值过期时间，默认和有值的过期时间一致，一般设置空值过期时间较短
	 */
	private Duration defaultNullValuesExpiration = null;

	/**
	 * 每个cacheName的过期时间，优先级比defaultExpiration高
	 */
	private Map<String, Duration> expires = new HashMap<>();

	/**
	 * 注解@Cachebale中分割过期时间的分隔符
	 */
	private String delimiter = "#";

	/**
	 * 缓存更新时通知其他节点的topic名称
	 */
	private String topic = "cache:redis:caffeine:topic";

}
