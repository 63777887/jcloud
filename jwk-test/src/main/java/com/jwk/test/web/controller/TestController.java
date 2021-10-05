package com.jwk.test.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jwk.common.core.model.RestResponse;
import com.jwk.test.netty.TestServerChannel;
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
  @SentinelResource(value = "/test")
  public RestResponse advertiseList(Long id) {

    return testService.getId(id);
  }

  @GetMapping("/test1")
  public void advertiseList(String msg) {
    TestServerChannel.channelGroup.forEach(channel -> {
      channel.writeAndFlush(msg);
    });
  }

}
