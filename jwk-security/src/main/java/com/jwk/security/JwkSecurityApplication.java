package com.jwk.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@MapperScan("com.jwk.security.web.dao")
public class JwkSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwkSecurityApplication.class, args);
    }

}
