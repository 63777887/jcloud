package com.jwk.upms.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwk.upms.base.entity.SysRoleMenu;
import com.jwk.upms.web.dao.SysRoleMenuMapper;
import com.jwk.upms.dto.SetRoleMenuDto;
import com.jwk.upms.web.service.SysRoleMenuService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * `sys_role_menu` 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

	@Override
	@Transactional
	public Boolean setRoleMenu(SetRoleMenuDto setRoleMenuDto) {
		getBaseMapper().deleteRoleMenu(setRoleMenuDto);
		if (CollUtil.isNotEmpty(setRoleMenuDto.getMenuIds())) {
			List<SysRoleMenu> roleMenuList = setRoleMenuDto.getMenuIds().stream().map(t -> {
				SysRoleMenu sysRoleMenu = new SysRoleMenu();
				sysRoleMenu.setRoleId(setRoleMenuDto.getRoleId());
				sysRoleMenu.setMenuId(t);
				return sysRoleMenu;
			}).collect(Collectors.toList());
			saveBatch(roleMenuList);
		}
		return Boolean.TRUE;
	}

}
