package com.jwk.test.service.inner;

import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.model.RestResponse.RestResponseBuilder;
import javax.validation.Valid;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class TestFallBackServiceImpl implements TestService {

  @Setter
  private Throwable cause;

  @Override
  public RestResponse getId(@Valid Long id) {
    log.error("feign 查询用户信息失败:{}", id, cause);
    return RestResponseBuilder.createSuccessBuilder("按客戶自定义,global handlerException----2")
        .buidler();
  }
}
