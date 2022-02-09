package com.jwk.api.api.handler;

import com.jwk.api.api.UaaRemoteService;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.core.model.InnerResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class UaaRemoteServiceFallBackService implements UaaRemoteService {

  @Setter
  private Throwable cause;

  @Override
  public InnerResponse<UserInfo> checkToken(String token) {
    log.error("feign checkToken fail:{}", token, cause);
    return null;
  }
}
