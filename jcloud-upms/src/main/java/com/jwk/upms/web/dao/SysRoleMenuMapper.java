package com.jwk.upms.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jwk.upms.base.entity.SysRoleMenu;
import com.jwk.upms.dto.SetRoleMenuDto;

/**
 * <p>
 * `sys_role_menu` Mapper 接口
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

	int deleteRoleMenu(SetRoleMenuDto setRoleMenuDto);

}
