package com.jwk.test.controller;

import com.jwk.common.model.RestResponse;
import com.jwk.test.service.inner.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web")
public class TestController {

  @Autowired
  private TestService testService;

  @GetMapping("/test")
  public RestResponse advertiseList(Long id){

    return RestResponse.RestResponseBuilder.createSuccessBuilder().setResult(testService.queryBrandListByOrganId(id)).buidler();
  }

}
