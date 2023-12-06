package com.jwk.common.log;

import com.jwk.common.log.event.SysLogListener;
import com.jwk.upms.base.api.LogRemoteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lengleng
 * @date 2019/2/1 日志自动配置
 */
@EnableAsync
@Configuration(proxyBeanMethods = false)
public class JwkLogAutoConfiguration {

	@Bean
	public SysLogListener sysLogListener(LogRemoteService logRemoteService) {
		return new SysLogListener(logRemoteService);
	}

}
