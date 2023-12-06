package com.jwk.common.knife4j.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 自动配置类
 * @date 2022/6/11
 */
@EnableConfigurationProperties(JwkSwaggerProperties.class)
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerAutoConfiguration {


	@Bean
	public Docket createRestApi(JwkSwaggerProperties jwkSwaggerProperties) {
		Predicate<RequestHandler> restPredicate = RequestHandlerSelectors.withClassAnnotation(RestController.class);
		Predicate<RequestHandler> classPredicate = RequestHandlerSelectors.withClassAnnotation(Controller.class);
		Predicate<RequestHandler> methodPredicate = RequestHandlerSelectors.withMethodAnnotation(ResponseBody.class);
		Predicate<RequestHandler> basePackagePredicate = RequestHandlerSelectors
				.basePackage(jwkSwaggerProperties.getBasePackage());
		restPredicate.or(classPredicate).or(methodPredicate).or(basePackagePredicate);

		return new Docket(DocumentationType.SWAGGER_2).groupName(jwkSwaggerProperties.getGroupName())
				.apiInfo(this.apiInfo(jwkSwaggerProperties)).useDefaultResponseMessages(false).select().apis(restPredicate).build();
	}

	private ApiInfo apiInfo(JwkSwaggerProperties jwkSwaggerProperties) {
		return new ApiInfoBuilder().title(jwkSwaggerProperties.getTitle())
				.description(jwkSwaggerProperties.getDescription()).version(jwkSwaggerProperties.getVersion()).build();
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
