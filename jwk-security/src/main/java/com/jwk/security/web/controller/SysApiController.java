package com.jwk.security.web.controller;


import com.jwk.common.model.RestResponse;
import com.jwk.security.web.entity.SysApi;
import com.jwk.security.web.service.SysApiService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * `sys_api` 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/sysApi")
public class SysApiController {

  @Autowired
  private SysApiService sysApiService;

  /**
   * 接口列表
   */
  @PostMapping(value = "/list")
  public RestResponse list() {
    List<SysApi> list = sysApiService.list();
    return RestResponse.RestResponseBuilder.createSuccessBuilder()
        .setResult(list).buidler();
  }

}
