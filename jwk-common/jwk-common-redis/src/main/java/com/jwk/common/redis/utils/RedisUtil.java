package com.jwk.common.redis.utils;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.jwk.common.redis.properties.RedisConfigProperties;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * Redis工具类
 * @date 2022/10/20
 */
@UtilityClass
@Slf4j
public class RedisUtil {

	private static final int threads = 10;

	private static final int nettyThreads = 10;

	private static final int masterConnectionMinimumIdleSize = 10;

	private static final int masterConnectionPoolSize = 64;

	private static final int slaveConnectionMinimumIdleSize = 10;

	private static final int slaveConnectionPoolSize = 64;

	private static final int scanInterval = 5000;

	/**
	 * 监控锁的看门狗超时时间单位为毫秒。该参数只适用于分布式锁的加锁请求中未明确使用leaseTimeout参数的情况,这里设置成我们接口的超时时间5s
	 */
	private static final int lockWatchdogTimeout = 5000;

	/**
	 * redssionConfig 构建
	 * @param redisProperties
	 * @return
	 */
	public Config config(RedisProperties redisProperties) {

		if (log.isDebugEnabled()) {
			log.debug("redisson init config start...RedisConfigProperties:{}", redisProperties);
		}

		// redisson配置文件
		Config config = new Config();
		config.setThreads(threads);
		config.setNettyThreads(nettyThreads);
		config.setLockWatchdogTimeout(lockWatchdogTimeout);

		// 使用json序列化方式
		Codec codec = new JsonJacksonCodec();
		config.setCodec(codec);

		if (redisProperties != null && redisProperties.getCluster() != null) {

			// 集群
			ClusterServersConfig clusterServers = config.useClusterServers();

			// 地址
			List<String> nodes = redisProperties.getCluster().getNodes();
			if (nodes != null && nodes.size() > 0) {
				nodes.stream().forEach(node -> {
					clusterServers.addNodeAddress("redis://" + node);
				});
			}

			// 密码
			String password = redisProperties.getPassword();
			String username = redisProperties.getUsername();
			if (StrUtil.isNotBlank(username)) {
				clusterServers.setUsername(username);
			}
			if (StrUtil.isNotBlank(password)) {
				clusterServers.setPassword(password);
			}

			clusterServers.setMasterConnectionMinimumIdleSize(masterConnectionMinimumIdleSize);
			clusterServers.setMasterConnectionPoolSize(masterConnectionPoolSize);
			clusterServers.setSlaveConnectionMinimumIdleSize(slaveConnectionMinimumIdleSize);
			clusterServers.setSlaveConnectionPoolSize(slaveConnectionPoolSize);
			clusterServers.setScanInterval(scanInterval);
		} else if (redisProperties != null && redisProperties.getSentinel() != null) {

			Sentinel sentinel = redisProperties.getSentinel();
			// 哨兵
			SentinelServersConfig sentinelServers = config.useSentinelServers();

			// 地址
			List<String> nodes = redisProperties.getSentinel().getNodes();
			if (nodes != null && nodes.size() > 0) {
				nodes.stream().forEach(node -> {
					sentinelServers.addSentinelAddress("redis://" + node);
				});
			}
			// 密码
			String password = redisProperties.getPassword();
			String username = redisProperties.getUsername();
			String master = sentinel.getMaster();
			String sentinelUsername = sentinel.getUsername();
			String sentinelPassword = sentinel.getPassword();
			if (StrUtil.isNotBlank(username)) {
				sentinelServers.setUsername(username);
			}
			if (StrUtil.isNotBlank(password)) {
				sentinelServers.setPassword(password);
			}
			if (StrUtil.isNotBlank(master)) {
				sentinelServers.setMasterName(master);
			}
			if (StrUtil.isNotBlank(sentinelUsername)) {
				sentinelServers.setSentinelUsername(sentinelUsername);
			}
			if (StrUtil.isNotBlank(sentinelPassword)) {
				sentinelServers.setSentinelPassword(sentinelPassword);
			}
			sentinelServers.setMasterConnectionMinimumIdleSize(masterConnectionMinimumIdleSize);
			sentinelServers.setMasterConnectionPoolSize(masterConnectionPoolSize);
			sentinelServers.setSlaveConnectionMinimumIdleSize(slaveConnectionMinimumIdleSize);
			sentinelServers.setSlaveConnectionPoolSize(slaveConnectionPoolSize);
			sentinelServers.setScanInterval(scanInterval);
		} else {
			if (redisProperties == null) {
				redisProperties = new RedisProperties();
				redisProperties.setHost("127.0.0.1");
				redisProperties.setPort(6379);
			}
			// 单机模式
			SingleServerConfig singleServer = config.useSingleServer();
			// 地址
			String host = redisProperties.getHost();
			// host = "localhost".equals(host)?"127.0.0.1":host;
			singleServer.setAddress("redis://" + host + StrPool.COLON + redisProperties.getPort());
			// 密码
			String password = redisProperties.getPassword();
			String username = redisProperties.getUsername();
			if (StrUtil.isNotBlank(username)) {
				singleServer.setUsername(username);
			}
			if (StrUtil.isNotBlank(password)) {
				singleServer.setPassword(password);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("redisson init config end. Config:{}", config);
		}

		return config;

	}

	/**
	 * redis键序列化使用StringRedisSerializer
	 */
	public RedisSerializer<String> keySerializer() {
		return RedisSerializer.string();
	}

	public RedisSerializer<Object> valueSerializer() {
		return RedisSerializer.java();
	}

	public RedisSerializer<String> stringValueSerializer() {
		return RedisSerializer.string();
	}

	public RedisSerializer<Object> jsonSerializer(ObjectProvider<ObjectMapper> objectProvider) {
		// jackson findAndRegisterModules，use copy
		ObjectMapper objectMapper = objectProvider.getIfAvailable(ObjectMapper::new).copy();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// findAndRegisterModules
		objectMapper.findAndRegisterModules();
		// class type info to json
		GenericJackson2JsonRedisSerializer.registerNullValueSerializer(objectMapper, null);
		objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}

	public String[] replaceName(String name, String delimiter) {
		return name.split(delimiter, 3);
	}

}
