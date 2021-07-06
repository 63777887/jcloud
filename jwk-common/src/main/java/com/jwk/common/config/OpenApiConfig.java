//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jwk.common.config;

import java.util.ArrayList;
import java.util.List;
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
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class OpenApiConfig {

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
//    Predicate<RequestHandler> predicate = Predicates
//        .or(restPredicate, Predicates.and(classPredicate, methodPredicate));
//    predicate = Predicates.and(predicate, basePackagePredicate);

//    ParameterBuilder ticketPar = new ParameterBuilder();
//    List<Parameter> pars = new ArrayList<Parameter>();
//    Parameter parameter = ticketPar.name(SecurityConstant.AUTH_KEY)
//        .description("user token")//Token 以及Authorization 为自定义的参数，session保存的名字是哪个就可以写成那个
//        .modelRef(new ModelRef("string")).parameterType("header")
//        .required(false).build();//header中的ticket参数非必填，传空也可以
//
//    pars.add(parameter);

    return new Docket(DocumentationType.SWAGGER_2)
        .groupName(this.groupName)
        .apiInfo(this.apiInfo())
        .useDefaultResponseMessages(false)
        .select()
        .apis(restPredicate)
        .build();
//        .globalOperationParameters(pars);
/*        .securitySchemes(securitySchemes())
        .securityContexts(securityContexts())*/
        // 赋予插件体系
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title(this.title)
        .description(this.description)
        .version(this.version)
        .build();
  }
/*
  private List<ApiKey> securitySchemes() {
    List<ApiKey> apiKeyList = new ArrayList<>();
    apiKeyList.add(new ApiKey("token", SecurityConstant.AUTH_KEY, "header"));
    return apiKeyList;
  }

  private List<SecurityContext> securityContexts() {
    List<SecurityContext> securityContexts = new ArrayList<>();
    securityContexts.add(
        SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("^(?!auth).*$"))
            .build());
    return securityContexts;
  }*/

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    List<SecurityReference> securityReferences = new ArrayList<>();
    securityReferences.add(new SecurityReference("token", authorizationScopes));
    return securityReferences;
  }


}
