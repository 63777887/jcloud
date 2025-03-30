package com.jwk.common.knife4j.config.config;

import com.fasterxml.classmate.TypeResolver;
import com.jwk.common.knife4j.config.condition.EnableOpenApiCondition;
import com.jwk.common.knife4j.config.properties.JwkSwaggerProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.PathProvider;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDocumentationScanner;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 自动配置类
 * @date 2022/6/11
 */

public class SwaggerConfiguration {

	@Bean
	@Conditional(EnableOpenApiCondition.class)
	public DocumentationPluginsBootstrapper documentationPluginsBootstrapper(
			DocumentationPluginsManager documentationPluginsManager, List<RequestHandlerProvider> handlerProviders,
			DocumentationCache scanned, ApiDocumentationScanner resourceListing, TypeResolver typeResolver,
			Defaults defaults, PathProvider pathProvider, Environment environment) {
		return new DocumentationPluginsBootstrapper(documentationPluginsManager, handlerProviders, scanned,
				resourceListing, typeResolver, defaults, pathProvider, environment);
	}

	@Bean
	public Docket createRestApi(JwkSwaggerProperties jwkSwaggerProperties) {
		Predicate<RequestHandler> restPredicate = RequestHandlerSelectors.withClassAnnotation(RestController.class);
		Predicate<RequestHandler> classPredicate = RequestHandlerSelectors.withClassAnnotation(Controller.class);
		Predicate<RequestHandler> methodPredicate = RequestHandlerSelectors.withMethodAnnotation(ResponseBody.class);
		Predicate<RequestHandler> basePackagePredicate = RequestHandlerSelectors
				.basePackage(jwkSwaggerProperties.getBasePackage());
		restPredicate.or(classPredicate).or(methodPredicate).or(basePackagePredicate);

		return new Docket(DocumentationType.SWAGGER_2).groupName(jwkSwaggerProperties.getGroupName())
				.apiInfo(this.apiInfo(jwkSwaggerProperties)).useDefaultResponseMessages(false).select()
				.apis(restPredicate).build();
	}

	private ApiInfo apiInfo(JwkSwaggerProperties jwkSwaggerProperties) {
		return new ApiInfoBuilder().title(jwkSwaggerProperties.getTitle())
				.description(jwkSwaggerProperties.getDescription()).version(jwkSwaggerProperties.getVersion()).build();
	}

}
