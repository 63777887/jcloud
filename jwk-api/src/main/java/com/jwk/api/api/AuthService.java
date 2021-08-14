package com.jwk.api.api;

import com.jwk.api.dto.RegisterReq;
import com.jwk.api.exception.InternalApiException;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "jwk-gateway", path = "/jwk-upms", contextId = "jwk-upms")
public interface AuthService {

  /**
   * 测试
   *
   * @param id
   * @return
   */
  @GetMapping("/inner/test")
  String getId(@Valid @RequestParam("id") Long id) throws InternalApiException;

  /**
   * 使用用户名密码换JWT令牌
   */
  @PostMapping(value = "/login")
  String login(@RequestParam String username, @RequestParam String password);

  /**
   * 注册用户
   */
  @PostMapping(value = "/register")
  String register(@RequestBody @Valid RegisterReq user);

  /**
   * 刷新令牌
   */
  @GetMapping(value = "/refreshToken")
  String refreshToken(@RequestHeader("${token.header}") String token);
}
