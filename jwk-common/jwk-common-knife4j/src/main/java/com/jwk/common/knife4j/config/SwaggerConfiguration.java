package com.jwk.common.knife4j.config;

import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@EnableSwagger2WebMvc
@EnableConfigurationProperties(JwkSwaggerProperties.class)
public class SwaggerConfiguration {

  @Autowired
  JwkSwaggerProperties jwkSwaggerProperties;


  @Bean
  public Docket createRestApi() {
    Predicate<RequestHandler> restPredicate = RequestHandlerSelectors
        .withClassAnnotation(RestController.class);
    Predicate<RequestHandler> classPredicate = RequestHandlerSelectors
        .withClassAnnotation(Controller.class);
    Predicate<RequestHandler> methodPredicate = RequestHandlerSelectors
        .withMethodAnnotation(ResponseBody.class);
    Predicate<RequestHandler> basePackagePredicate = RequestHandlerSelectors
        .basePackage(jwkSwaggerProperties.getBasePackage());
    restPredicate.or(classPredicate).or(methodPredicate).or(basePackagePredicate);

    return new Docket(DocumentationType.SWAGGER_2)
        .groupName(jwkSwaggerProperties.getGroupName())
        .apiInfo(this.apiInfo())
        .useDefaultResponseMessages(false)
        .select()
        .apis(restPredicate)
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title(jwkSwaggerProperties.getTitle())
        .description(jwkSwaggerProperties.getDescription())
        .version(jwkSwaggerProperties.getVersion())
        .build();
  }

}
