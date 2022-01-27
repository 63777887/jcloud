package com.jwk.upms.web.controller;


import com.jwk.common.core.model.RestResponse;
import com.jwk.upms.web.entity.SysApi;
import com.jwk.upms.web.service.SysApiService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
  @GetMapping(value = "/list")
  public RestResponse list() {
    List<SysApi> list = sysApiService.list();
    return RestResponse.RestResponseBuilder.createSuccessBuilder()
        .setResult(list).buidler();
  }

}
