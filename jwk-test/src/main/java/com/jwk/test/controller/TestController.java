package com.jwk.test.controller;

import com.jwk.common.model.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/advertise")
public class TestController {

  @GetMapping("/test")
  public RestResponse advertiseList(){

    return RestResponse.RestResponseBuilder.createSuccessBuilder().buidler();
  }

}
