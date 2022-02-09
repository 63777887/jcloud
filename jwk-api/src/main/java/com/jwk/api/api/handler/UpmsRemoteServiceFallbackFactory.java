package com.jwk.api.api.handler;

import com.jwk.api.api.UpmsRemoteService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 服务降级
 */
@Component
public class UpmsRemoteServiceFallbackFactory implements FallbackFactory<UpmsRemoteService> {

  @Override
  public UpmsRemoteService create(Throwable throwable) {
    UpmsRemoteServiceFallBackService upmsRemoteServiceFallBackService = new UpmsRemoteServiceFallBackService();
    upmsRemoteServiceFallBackService.setCause(throwable);
    return upmsRemoteServiceFallBackService;
  }

}
