package com.jwk.common.redis.properties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.3
 * <p>
 * Redis 相关配置
 */
@Data
public class RedisCacheConfigProp {

	/**
	 * 全局过期时间，默认不过期
	 */
	private Duration defaultExpiration = Duration.ZERO;

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

	/**
	 * 生成当前节点id的key，当配置了jwk.cache.multi.server-id时，该配置不生效
	 */
	private String serverIdGeneratorKey = "cache:redis:caffeine:server-id";

}
