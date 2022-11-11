package com.jwk.common.knife4j.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 配置文件
 */
@ConfigurationProperties(prefix = "jwk.swagger")
@Data
public class JwkSwaggerProperties {

	/**
	 * 应用名
	 */
	@Value("${spring.application.name}")
	private String title;

	/**
	 * 详情介绍
	 */
	private String description;

	/**
	 * 版本
	 */
	private String version = "1.0";

	/**
	 * 群组名
	 */
	@Value("${spring.application.name}")
	private String groupName;

	/**
	 * 接口基础路径
	 */
	private String basePackage = "com.jwk";

}
