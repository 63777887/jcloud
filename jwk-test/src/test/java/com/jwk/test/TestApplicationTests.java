package com.jwk.test;

import com.jwk.test.jvm.MethodHandleTest;
import java.lang.reflect.InvocationTargetException;
import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class TestApplicationTests {

//  @Autowired
//  MethodHandleTest methodHandleTest;
  @Test
  void contextLoads()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

    MethodHandleTest methodHandleTest =new MethodHandleTest();
    new MockUp<MethodHandleTest>(){
      @Mock
      private boolean testOne(boolean bool){
        System.out.println(bool);
        return false;
      }
    };
//    System.out.println(testOne.invoke(methodHandleTestClass.newInstance(),true));


    System.out.println(methodHandleTest.testTwo(true));

  }

}
