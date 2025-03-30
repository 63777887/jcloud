/*
 *    Copyright (c) 2018-2025, icar All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: icar
 */
package com.jwk.common.knife4j.config.config;

import cn.hutool.core.util.StrUtil;
import com.jwk.common.knife4j.config.annotation.EnableOpenApi;
import com.jwk.common.knife4j.config.properties.JwkSwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.http.HttpHeaders;

/**
 * swagger配置
 *
 * <p>
 * 禁用方法1：使用注解@Profile({"dev","test"})
 * <p>
 * 表示在开发或测试环境开启，而在生产关闭。（推荐使用） 禁用方法2：使用注解@ConditionalOnProperty(name = "swagger.enable",
 * <p>
 * havingValue = "true") 然后在测试配置或者开发配置中添加swagger.enable=true即可开启，生产环境不填则默认关闭Swagger.
 * </p>
 *
 * @author icar
 */
@RequiredArgsConstructor
public class OpenAPIDefinition extends OpenAPI
		implements InitializingBean, ApplicationContextAware, ImportBeanDefinitionRegistrar {

	private ApplicationContext applicationContext;

	@Setter
	private String path;

	private SecurityScheme securityScheme(JwkSwaggerProperties swaggerProperties) {
		OAuthFlow clientCredential = new OAuthFlow();
		clientCredential.setTokenUrl(swaggerProperties.getTokenUrl());
		clientCredential.setScopes(new Scopes().addString(swaggerProperties.getScope(), swaggerProperties.getScope()));
		OAuthFlows oauthFlows = new OAuthFlows();
		oauthFlows.password(clientCredential);
		SecurityScheme securityScheme = new SecurityScheme();
		securityScheme.setType(SecurityScheme.Type.OAUTH2);
		securityScheme.setFlows(oauthFlows);
		return securityScheme;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnableOpenApi.class.getName(),
				true);
		Object value = annotationAttributes.get("value");
		if (Objects.isNull(value)) {
			return;
		}
		//
		// BeanDefinitionBuilder definition =
		// BeanDefinitionBuilder.genericBeanDefinition(OpenAPIDefinition.class);
		// definition.addPropertyValue("path", value);
		// definition.setPrimary(true);
		this.path = value.toString();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		JwkSwaggerProperties swaggerProperties = applicationContext.getBean(JwkSwaggerProperties.class);
		this.info(new Info().title(swaggerProperties.getTitle()));
		// oauth2.0 password
		this.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme(swaggerProperties));
		// servers
		List<Server> serverList = new ArrayList<>();

		if (StrUtil.isBlank(path)) {
			ServerProperties serverProperties = applicationContext.getBean(ServerProperties.class);
			path = serverProperties.getServlet().getContextPath();
		}
		serverList.add(new Server().url(swaggerProperties.getHost() + "/" + path));
		this.servers(serverList);
		// 支持参数平铺
		// SpringDocUtils.getConfig().addSimpleTypesForParameterObject(Class.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
