package com.jwk.gateway;

import java.util.UUID;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(JwkGatewayApplication.class);
  }

  public static void main(String[] args) {
    System.out.println(UUID.randomUUID().toString().replace("-", ""));
  }
}
