package com.jwk.common.redis.support;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jiwk
 * @version 0.1.1
 * <p>
 * 缓存的Topic消息对象
 * @date 2022/10/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheMessage implements Serializable {

	private String cacheName;

	private Object key;

	private Object cacheServerId;

}
