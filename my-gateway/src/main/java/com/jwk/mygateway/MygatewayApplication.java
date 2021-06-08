package com.jwk.mygateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MygatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(MygatewayApplication.class, args);
  }

}
