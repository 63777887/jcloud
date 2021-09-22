package com.jwk.test.service.inner;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@Component
public class RemoteUserServiceFallbackFactory implements FallbackFactory<TestService> {

  @Override
  public TestService create(Throwable throwable) {
    TestFallBackService remoteUserServiceFallback = new TestFallBackService();
    remoteUserServiceFallback.setCause(throwable);
    return remoteUserServiceFallback;
  }

}
