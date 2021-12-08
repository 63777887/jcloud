package com.jwk.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAdminServer
public class JwkAdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwkAdminApplication.class, args);
  }

//  //加入如下配置
//  @Bean({"threadPoolTaskExecutor", "webMvcAsyncTaskExecutor"})
//  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
//    return new ThreadPoolTaskExecutor();
//  }
}
