package com.jwk.common.redis.support;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author Jiwk
 * @version 0.1.1
 * <p>
 * Redis监听缓存Topic，多节点同步删除缓存
 * @date 2022/10/10
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class CacheMessageListener implements MessageListener {

	private final RedisSerializer<Object> redisSerializer;

	private final RedisCaffeineCacheManager redisCaffeineCacheManager;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		CacheMessage cacheMessage = (CacheMessage) redisSerializer.deserialize(message.getBody());
		if (log.isDebugEnabled()) {
			log.debug(
					"receive a redis topic message, clear local cache, the cacheName is {}, the key is {}，"
							+ "the cacheServerId is {}",
					cacheMessage.getCacheName(), cacheMessage.getKey(), cacheMessage.getCacheServerId());
		}
		if (!ObjectUtil.equals(cacheMessage.getCacheServerId(), redisCaffeineCacheManager.getCacheSeverId())) {
			redisCaffeineCacheManager.clearLocal(cacheMessage.getCacheName(), cacheMessage.getKey());
		}
	}

}
