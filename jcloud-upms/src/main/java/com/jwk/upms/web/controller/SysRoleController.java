package com.jwk.upms.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.common.core.model.R;
import com.jwk.upms.base.entity.SysRole;
import com.jwk.upms.dto.RoleDto;
import com.jwk.upms.dto.SetRoleMenuDto;
import com.jwk.upms.enums.ErrorCodeStatusE;
import com.jwk.upms.web.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * `sys_role` 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

	@Autowired
	SysRoleService sysRoleService;

	@GetMapping("/all")
	public R getAllMenu() {
		return R.ok(sysRoleService.list());
	}

	/**
	 * 用户列表
	 */
	@GetMapping(value = "/list")
	public R list(Page<SysRole> page, RoleDto roleDto) {
		return R.ok(sysRoleService.getRoleList(page, roleDto));
	}

	/**
	 * 用户列表
	 */
	@PostMapping(value = "/add")
	@PreAuthorize("@pms.hasPermission('sys_role_add')")
	public R add(@RequestBody @Validated RoleDto roleDto) {
		return R.ok(sysRoleService.saveRole(roleDto));
	}

	/**
	 * 根据ID查找用户信息
	 */
	@GetMapping(value = "/{id}")
	public R getUserById(@PathVariable Long id) {
		return R.ok(sysRoleService.getRoleById(id));
	}

	/**
	 * 更新用户信息
	 */
	@PostMapping(value = "/update")
	@PreAuthorize("@pms.hasPermission('sys_role_update')")
	public R update(@RequestBody @Validated RoleDto roleDto) {

		return R.ok(sysRoleService.updateRole(roleDto));
	}

	/**
	 * 根据ID删除角色信息
	 */
	@DeleteMapping(value = "/delete/{id}")
	@PreAuthorize("@pms.hasPermission('sys_role_del')")
	public R delete(@PathVariable Long id) {
		return R.ok(sysRoleService.deleteRole(id));
	}

	@PostMapping("/setRoleMenu")
	@PreAuthorize("@pms.hasPermission('sys_role_update_menu')")
	public R setRoleMenu(@RequestBody SetRoleMenuDto setRoleMenuDto) {
		if (setRoleMenuDto.getRoleId() == null || setRoleMenuDto.getRoleId() <= 0) {
			throw new ServiceException(ErrorCodeStatusE.ROLE_ID_EMPTY.getCode(),
					ErrorCodeStatusE.ROLE_ID_EMPTY.getMsg());
		}
		return R.ok(sysRoleService.setRoleMenu(setRoleMenuDto));
	}

}
