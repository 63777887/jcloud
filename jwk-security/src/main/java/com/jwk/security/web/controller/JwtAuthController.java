package com.jwk.security.web.controller;

import com.jwk.common.model.RestResponse;
import com.jwk.security.security.service.impl.JwtAuthService;
import com.jwk.security.web.dto.RegisterReq;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JWT获取令牌和刷新令牌接口
 */
@RestController()
@RequestMapping("/admin")
public class JwtAuthController {

  @Resource
  private JwtAuthService jwtAuthService;

  /**
   * 使用用户名密码换JWT令牌
   */
  @PostMapping(value = "/login")
  public RestResponse login(@RequestBody Map<String, String> map) {

    String username = map.get("username");
    String password = map.get("password");

        if(StringUtils.isEmpty(username)
                || StringUtils.isEmpty(password)){
            return RestResponse.RestResponseBuilder.createFailBuilder("用户名或者密码不能为空").buidler();
        }
    return RestResponse.RestResponseBuilder.createSuccessBuilder()
        .setResult(jwtAuthService.login(username, password, null)).buidler();
  }


  /**
   * 注册用户
   */
  @PostMapping(value = "/register")
  public RestResponse register(@RequestBody @Valid RegisterReq user) {
    jwtAuthService.register(user);
    return RestResponse.RestResponseBuilder.createSuccessBuilder()
        .setResult("注册成功").buidler();
  }

  /**
   * 刷新令牌
   */
  @GetMapping(value = "/refreshToken")
  public RestResponse refreshToken(@RequestHeader("${token.header}") String token) {
    return RestResponse.RestResponseBuilder.createSuccessBuilder().setResult(jwtAuthService.refreshToken(token)).buidler();
  }


}
