//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jwk.common.config;

import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfiguration {

  @Value("${jwk.swagger.title:${spring.application.name}}")
  private String title;
  @Value("${jwk.swagger.description:}")
  private String description;
  @Value("${jwk.swagger.version:1.0}")
  private String version;
  @Value("${jwk.swagger.namespace:${spring.application.name}}")
  private String groupName;
  @Value("${jwk.swagger.basePackage:com.jwk}")
  private String basePackage;


  @Bean
  public Docket createRestApi() {
    Predicate<RequestHandler> restPredicate = RequestHandlerSelectors
        .withClassAnnotation(RestController.class);
    Predicate<RequestHandler> classPredicate = RequestHandlerSelectors
        .withClassAnnotation(Controller.class);
    Predicate<RequestHandler> methodPredicate = RequestHandlerSelectors
        .withMethodAnnotation(ResponseBody.class);
    Predicate<RequestHandler> basePackagePredicate = RequestHandlerSelectors
        .basePackage(this.basePackage);
    restPredicate.or(classPredicate).or(methodPredicate).or(basePackagePredicate);

    return new Docket(DocumentationType.SWAGGER_2)
        .groupName(this.groupName)
        .apiInfo(this.apiInfo())
        .useDefaultResponseMessages(false)
        .select()
        .apis(restPredicate)
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title(this.title)
        .description(this.description)
        .version(this.version)
        .build();
  }

}
