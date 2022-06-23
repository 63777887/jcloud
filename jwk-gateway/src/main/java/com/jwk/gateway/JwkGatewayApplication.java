package com.jwk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class JwkGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwkGatewayApplication.class, args);
  }

}
