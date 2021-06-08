package com.jwk.down;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@MapperScan("com.jwk.down.web.dao")
public class DownApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownApplication.class, args);
    }

}
