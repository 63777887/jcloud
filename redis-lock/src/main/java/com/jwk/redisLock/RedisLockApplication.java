package com.jwk.redisLock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: parent
 * @author: jwk
 * @create: {MONTH}-{HOUR}:2021/4/2--上午12:39
 **/

@SpringBootApplication
@ServletComponentScan
//@EnableDiscoveryClient
public class RedisLockApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(RedisLockApplication.class, args);
    }
}
