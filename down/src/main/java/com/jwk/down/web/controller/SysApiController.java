package com.jwk.down.web.controller;


import com.jwk.down.common.RestResponse;
import com.jwk.down.web.entity.SysApi;
import com.jwk.down.web.entity.SysUser;
import com.jwk.down.web.service.SysApiService;
import com.jwk.down.web.service.SysUserService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
