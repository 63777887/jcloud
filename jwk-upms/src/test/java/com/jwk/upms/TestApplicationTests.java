package com.jwk.upms;

import com.jwk.upms.web.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = UpmsApplication.class)
class TestApplicationTests {

//  @Autowired
//  MethodHandleTest methodHandleTest;

  @Autowired
private SysUserService sysUserService;
  @Test
  void contextLoads() {

  }

  @Test
  void test1() {
    sysUserService.lambdaQuery().list().forEach(System.out::println);
  }

}
