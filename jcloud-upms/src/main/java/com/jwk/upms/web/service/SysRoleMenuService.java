package com.jwk.upms.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jwk.upms.base.entity.SysRoleMenu;
import com.jwk.upms.dto.SetRoleMenuDto;

/**
 * <p>
 * `sys_role_menu` 服务类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

	Boolean setRoleMenu(SetRoleMenuDto setRoleMenuDto);

}
