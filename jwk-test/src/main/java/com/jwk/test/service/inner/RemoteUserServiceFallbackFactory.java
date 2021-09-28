package com.jwk.test.service.inner;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 服务降级
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
