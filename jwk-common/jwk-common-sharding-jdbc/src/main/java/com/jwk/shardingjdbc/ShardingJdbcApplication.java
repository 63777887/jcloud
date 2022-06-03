package com.jwk.shardingjdbc;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan("com.jwk.shardingjdbc.mapper")
public class ShardingJdbcApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShardingJdbcApplication.class, args);
  }

}
