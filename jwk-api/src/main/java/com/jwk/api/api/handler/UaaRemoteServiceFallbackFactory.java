package com.jwk.api.api.handler;

import com.jwk.api.api.UaaRemoteService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 服务降级
 */
@Component
public class UaaRemoteServiceFallbackFactory implements FallbackFactory<UaaRemoteService> {

  @Override
  public UaaRemoteService create(Throwable throwable) {
    UaaRemoteServiceFallBackService uaaRemoteServiceFallBackService = new UaaRemoteServiceFallBackService();
    uaaRemoteServiceFallBackService.setCause(throwable);
    return uaaRemoteServiceFallBackService;
  }

}
