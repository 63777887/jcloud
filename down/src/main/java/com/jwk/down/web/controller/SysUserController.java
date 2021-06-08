package com.jwk.down.web.controller;


import com.jwk.down.common.RestResponse;
import com.jwk.down.web.entity.SysUser;
import com.jwk.down.web.service.SysUserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  @PostMapping(value = "/list")
  public RestResponse list() {

    return RestResponse.RestResponseBuilder.createSuccessBuilder()
        .setResult(sysUserService.list()).buidler();
  }

}
