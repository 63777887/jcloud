package com.jwk.test.listen;

import java.time.Duration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 测试监听事件
 * @date 2022/9/16
 */
public class TestSpringApplicationRunnerListener implements
    SpringApplicationRunListener,
    ApplicationContextAware {


  @Autowired
  ApplicationContext applicationContext;


  public TestSpringApplicationRunnerListener(SpringApplication application, String[] args) {
  }

  @Override
  public void starting(ConfigurableBootstrapContext bootstrapContext) {

    System.out.println("--------------------------");
    System.out.println("starting...");
  }

  @Override
  public void started(ConfigurableApplicationContext context, Duration timeTaken) {
//    applicationContext.publishEvent(new TestApplicationEvent("test..."));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
