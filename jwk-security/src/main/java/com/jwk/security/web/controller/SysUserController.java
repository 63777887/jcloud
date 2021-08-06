package com.jwk.security.web.controller;


import com.jwk.common.model.RestResponse;
import com.jwk.security.web.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@Api(tags = "SysUserController API", description = "系统-用户", produces = MediaType.APPLICATION_JSON_VALUE)
public class SysUserController {

  @Autowired
  private SysUserService sysUserService;

  /**
   * 用户列表
   */
  @ApiOperation(value = "用户列表")
  @GetMapping(value = "/list")
  public RestResponse list() {

    return RestResponse.RestResponseBuilder.createSuccessBuilder()
        .setResult(sysUserService.list()).buidler();
  }

}
