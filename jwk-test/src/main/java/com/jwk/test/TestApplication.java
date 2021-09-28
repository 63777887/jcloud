package com.jwk.test;

import com.jwk.common.fegin.annotation.EnableJwkFeignClients;
import com.jwk.security.security.annotation.EnableJwkResourceServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJwkFeignClients
@EnableJwkResourceServer
public class TestApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestApplication.class);

  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext = SpringApplication
        .run(TestApplication.class, args);
    ConfigurableEnvironment environment = applicationContext.getEnvironment();
    String port = environment.getProperty("server.port");
    LOGGER.info("接口聚合文档地址：{}{}{}{}", "http://127.0.0.1:", port, "/", "doc.html");
  }

}
