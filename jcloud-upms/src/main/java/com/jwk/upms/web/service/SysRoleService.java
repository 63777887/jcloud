package com.jwk.upms.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jwk.upms.base.entity.SysRole;
import com.jwk.upms.dto.RoleDto;
import com.jwk.upms.dto.SetRoleMenuDto;

/**
 * <p>
 * `sys_role` 服务类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
public interface SysRoleService extends IService<SysRole> {

	Page<SysRole> getRoleList(Page<SysRole> page, RoleDto roleDto);

	Boolean saveRole(RoleDto roleDto);

	SysRole getRoleById(Long id);

	Boolean updateRole(RoleDto roleDto);

	Boolean deleteRole(Long roleId);

	Boolean setRoleMenu(SetRoleMenuDto setRoleMenuDto);

}
