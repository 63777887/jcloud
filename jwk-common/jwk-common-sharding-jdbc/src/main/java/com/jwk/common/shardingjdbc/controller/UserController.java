package com.jwk.common.shardingjdbc.controller;

import com.jwk.common.shardingjdbc.entity.User;
import com.jwk.common.shardingjdbc.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-09-28
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * 用户列表
   */
  @GetMapping(value = "/list")
  public List<User> list() {

    return userService.list();
  }

}
