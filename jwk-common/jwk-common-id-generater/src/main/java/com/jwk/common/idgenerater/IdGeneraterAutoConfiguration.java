package com.jwk.common.idgenerater;

import com.jwk.common.idgenerater.manager.IdGeneratorManage;
import com.jwk.common.idgenerater.manager.impl.RedisGeneratorManage;
import com.jwk.common.idgenerater.properties.IdGeneraterProperties;
import com.jwk.common.idgenerater.service.IdGeneratorService;
import com.jwk.common.idgenerater.service.impl.IdGeneratorServiceImpl;
import com.jwk.common.redis.lock.RedisLockService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * 自动注入
 * @date 2022/11/2
 */
@Configuration
@EnableConfigurationProperties(IdGeneraterProperties.class)
@Import({IdGeneratorServiceImpl.class,RedisGeneratorManage.class})
public class IdGeneraterAutoConfiguration {

	@Bean
	public KeyGenerator keyGenerator() {
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(method.getName());
			for (Object obj : params) {
				sb.append(obj.toString());
			}
			return sb.toString();
		};
	}

}
