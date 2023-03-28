package com.jwk.common.redis.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Jiwk
 * @version 0.1.1
 * <p>
 * Redis配置项
 * @date 2022/10/10
 */
@Data
@ConfigurationProperties(prefix = "jwk")
public class RedisConfigProperties {

	@NestedConfigurationProperty
	private RedisProperties redis;

}
