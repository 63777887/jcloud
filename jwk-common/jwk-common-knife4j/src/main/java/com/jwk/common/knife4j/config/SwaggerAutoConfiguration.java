package com.jwk.common.knife4j.config;

import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.jwk.common.knife4j.config.annotation.EnableOpenApi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 自动配置类
 * @date 2022/6/11
 */

@Configuration
@Slf4j
@Import(Knife4jProperties.class)
public class SwaggerAutoConfiguration {

	/**
	 * 自定义ProductionSecurityFilter 增加@EnableOpenApi注解权限控制
	 * {@link com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration#productionSecurityFilter}
	 * @param knife4jProperties
	 * @param environment
	 * @param applicationContext
	 * @return
	 */
	@Bean
	public ProductionSecurityFilter productionSecurityFilter(Knife4jProperties knife4jProperties,
			Environment environment, ApplicationContext applicationContext) {
		// 获取所有使用 @EnableOpenApi 注解的 Bean 定义
		Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableOpenApi.class);
		// 如果结果不为空，说明存在使用 @EnableOpenApi 注解的 Bean

		boolean prod = false;
		ProductionSecurityFilter p = null;
		if (knife4jProperties == null) {
			if (environment != null) {
				String prodStr = environment.getProperty("knife4j.production");
				if (log.isDebugEnabled()) {
					log.debug("swagger.production:{}", prodStr);
				}
				prod = Boolean.valueOf(prodStr);
			}
			p = new ProductionSecurityFilter(prod);
		}
		else if (beansWithAnnotation.isEmpty()) {
			p = new ProductionSecurityFilter(true);
		}
		else {
			p = new ProductionSecurityFilter(knife4jProperties.isProduction());
		}

		return p;
	}

	/**
	 * 解决springboot2.6.x之后，swagger不生效问题 注入这个bean之后，配置文件加上
	 * spring.mvc.pathmatch.matching-strategy=ANT_PATH_MATCHER
	 * @param webEndpointsSupplier
	 * @param servletEndpointsSupplier
	 * @param controllerEndpointsSupplier
	 * @param endpointMediaTypes
	 * @param corsProperties
	 * @param webEndpointProperties
	 * @param environment
	 * @return
	 */
	@Bean
	public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
			ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
			EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
			WebEndpointProperties webEndpointProperties, Environment environment) {

		List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
		Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
		allEndpoints.addAll(webEndpoints);
		allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
		allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
		String basePath = webEndpointProperties.getBasePath();
		EndpointMapping endpointMapping = new EndpointMapping(basePath);
		boolean shouldRegisterLinksMapping = webEndpointProperties.getDiscovery().isEnabled()
				&& (org.springframework.util.StringUtils.hasText(basePath)
						|| ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));

		return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
				corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
				shouldRegisterLinksMapping, null);
	}

}
