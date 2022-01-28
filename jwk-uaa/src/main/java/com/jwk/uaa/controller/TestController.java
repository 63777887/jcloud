package com.jwk.uaa.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.model.RestResponse.RestResponseBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {

  @GetMapping("/test")
  @SentinelResource(value = "/test")
  public RestResponse advertiseList(Long id) {

    return RestResponseBuilder.createSuccessBuilder().setResult(id).buidler();
  }

}
