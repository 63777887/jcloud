package com.jwk.upms.web.controller;


import com.jwk.common.core.model.RestResponse;
import com.jwk.upms.web.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * `sys_user` 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

  @Autowired
  private SysUserService sysUserService;

  /**
   * 用户列表
   */
  @GetMapping(value = "/list")
  public RestResponse list() {

    return RestResponse.RestResponseBuilder.createSuccessBuilder()
        .setResult(sysUserService.list()).buidler();
  }

}
