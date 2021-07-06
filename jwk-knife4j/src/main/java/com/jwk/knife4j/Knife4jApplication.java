package com.jwk.knife4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class Knife4jApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(Knife4jApplication.class);

  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext = SpringApplication
        .run(Knife4jApplication.class, args);
    ConfigurableEnvironment environment = applicationContext.getEnvironment();
    String port = environment.getProperty("server.port");
    LOGGER.info("接口聚合文档地址：{}{}{}{}", "http://127.0.0.1:", port, "/", "doc.html");
  }

}
