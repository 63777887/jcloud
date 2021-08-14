package com.jwk.api.api;

import com.jwk.api.exception.InternalApiException;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "jwk-gateway", path = "/jwk-security", contextId = "jwk-gateway")
public interface TestService {

  /**
   * 测试
   *
   * @param id
   * @return
   */
  @GetMapping("/inner/test")
  String getId(@Valid @RequestParam("id") Long id) throws InternalApiException;


}
