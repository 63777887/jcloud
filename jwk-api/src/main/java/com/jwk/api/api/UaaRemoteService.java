package com.jwk.api.api;

import com.jwk.api.constant.ServerNameConstants;
import com.jwk.api.dto.UserInfo;
import com.jwk.api.exception.InternalApiException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = ServerNameConstants.SERVER_UAA,contextId = "testService")
public interface UaaRemoteService {

  /**
   * 校验token
   *
   * @param
   * @return
   */
  @GetMapping("/inner/test")
  UserInfo checkToken(@RequestParam("token") String token) throws InternalApiException;


}
