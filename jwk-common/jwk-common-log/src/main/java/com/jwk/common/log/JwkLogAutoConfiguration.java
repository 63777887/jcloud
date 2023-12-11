package com.jwk.common.log;

import com.jwk.common.log.event.SysLogListener;
import com.jwk.upms.base.api.LogRemoteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 自动配置类
 * @date 2023/12/11
 */
@EnableAsync
@Configuration(proxyBeanMethods = false)
public class JwkLogAutoConfiguration {

	@Bean
	public SysLogListener sysLogListener(LogRemoteService logRemoteService) {
		return new SysLogListener(logRemoteService);
	}

}
