package com.jwk.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwk.swagger")
@Data
public class JwkSwaggerProperties {
  @Value("${spring.application.name}")
  private String title;
  private String description;
  private String version ="1.0";
  @Value("${spring.application.name}")
  private String groupName;
  private String basePackage = "com.jwk";

}
