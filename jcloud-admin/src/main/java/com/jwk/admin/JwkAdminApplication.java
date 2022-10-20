package com.jwk.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Springboot启动类
 */
@SpringBootApplication
@EnableAdminServer
public class JwkAdminApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwkAdminApplication.class);


	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(JwkAdminApplication.class, args);
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		String port = environment.getProperty("server.port");
		LOGGER.info("Admin控制台地址：{}{}", "http://127.0.0.1:", port);
	}

	// //加入如下配置
	// @Bean({"threadPoolTaskExecutor", "webMvcAsyncTaskExecutor"})
	// public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
	// return new ThreadPoolTaskExecutor();
	// }

}
