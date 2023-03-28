package com.jwk.upms.web.controller;

import com.jwk.common.core.model.InnerResponse;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.security.annotation.Inner;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.web.service.AuthService;
import com.jwk.upms.web.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	AuthService authService;

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 根据用户名查找用户
	 */
	@Inner
	@GetMapping(value = "/findUserByName")
	public InnerResponse<UserInfo> findUserByName(@RequestParam("name") String name) {
		return InnerResponse.success(authService.findUserByName(name));
	}

	/**
	 * 根据手机号查找用户
	 */
	@Inner
	@GetMapping(value = "/findUserByPhone")
	public InnerResponse<UserInfo> findUserByPhone(@RequestParam("phone") String phone) {
		return InnerResponse.success(authService.findUserByPhone(phone));
	}

	/**
	 * 根据邮箱查找用户
	 */
	@Inner
	@GetMapping(value = "/findUserByEmail")
	public InnerResponse<UserInfo> findUserByEmail(@RequestParam("email") String mail) {
		return InnerResponse.success(authService.findUserByEmail(mail));
	}

	/**
	 * 用户列表
	 */
	@GetMapping(value = "/list")
	@PreAuthorize("@pms.hasPermission()")
	public RestResponse list() {

		return RestResponse.success(sysUserService.list());
	}

}
