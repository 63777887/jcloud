//package com.jwk.test;
//
//import com.jwk.test.jvm.MethodHandleTest;
//import java.lang.reflect.InvocationTargetException;
////import mockit.Mock;
////import mockit.MockUp;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//class TestApplicationTests {
//
////  @Autowired
////  MethodHandleTest methodHandleTest;
//  @Autowired
//private ResourceServerTokenServices tokenServices;
//  @Test
//  void contextLoads()
//      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
//
////    MethodHandleTest methodHandleTest =new MethodHandleTest();
////    new MockUp<MethodHandleTest>(){
////      @Mock
////      private boolean testOne(boolean bool){
////        System.out.println(bool);
////        return false;
////      }
////    };
//////    System.out.println(testOne.invoke(methodHandleTestClass.newInstance(),true));
////
////
////    System.out.println(methodHandleTest.testTwo(true));
//    System.out.println(tokenServices.loadAuthentication("token"));
//  }
//
//}
