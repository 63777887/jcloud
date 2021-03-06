package com.jwk.test.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.security.security.annotation.Inner;
import com.jwk.common.security.security.annotation.UserParam;
import com.jwk.common.security.security.dto.SysUser;
import com.jwk.test.netty.TestServerChannel;
import com.jwk.test.service.inner.TestService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
//  @Autowired
//  private ResourceServerTokenServices tokenServices;

  @Autowired
  private RemoteTokenServices remoteTokenServices;
  @GetMapping("/test2")
  public SysUser test2(@UserParam SysUser sysUser) {
    return sysUser;
  }

  @PostMapping("/export1")
  @Inner
  public String export1(
      HttpServletResponse response, HttpServletRequest request) throws IOException {
    HSSFWorkbook workbook = new HSSFWorkbook();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename="+"stat".concat(".xls"));
    workbook.write(response.getOutputStream());
    response.flushBuffer();
    return "????????????";
  }

}
