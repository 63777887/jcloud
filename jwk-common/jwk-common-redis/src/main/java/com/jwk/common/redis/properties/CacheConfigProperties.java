package com.jwk.common.redis.properties;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Jiwk
 * @date 2022/10/10
 * @version 0.1.1
 * <p>
 * 缓存配置项
 */
@Data
@ConfigurationProperties(prefix = "jwk.cache.multi")
public class CacheConfigProperties {

	/**
	 * 需要添加缓存的cacheName，开启 dynamic=ture 后无需配置
	 */
	private Set<String> cacheNames = new HashSet<>();

	/**
	 * 是否存储空值，默认true，防止缓存穿透
	 */
	private boolean cacheNullValues = true;

	/**
	 * 是否动态根据cacheName创建Cache的实现，默认true
	 */
	private boolean dynamic = true;

	/**
	 * 缓存key的前缀
	 */
	private String cachePrefix;

	/**
	 * 当前节点id。来自当前节点的缓存更新通知不会被处理
	 */
	private Object serverId;

	@NestedConfigurationProperty
	private RedisCacheConfigProp redis = new RedisCacheConfigProp();

	@NestedConfigurationProperty
	private CaffeineCacheConfigProp caffeine = new CaffeineCacheConfigProp();

}
