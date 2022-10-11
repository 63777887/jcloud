package com.jwk.test.listen;

import org.springframework.context.ApplicationEvent;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 测试监听事件
 * @date 2022/9/16
 */
public class TestApplicationEvent extends ApplicationEvent {

  public TestApplicationEvent(Object source) {
    super(source);
  }
}
