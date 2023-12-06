package com.jwk.upms.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysRoleMenu;
import com.jwk.upms.web.dao.SysMenuMapper;
import com.jwk.upms.enums.MenuStatusE;
import com.jwk.upms.web.service.SysMenuService;
import com.jwk.upms.web.service.SysRoleMenuService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * `sys_menu` 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

	private final SysRoleMenuService sysRoleMenuService;

	@Override
	public List<SysMenu> getMenuListByRole(Long roleId, Integer menuType) {
		List<SysMenu> sysMenus = new ArrayList<>();
		List<SysRoleMenu> roleMenuList = sysRoleMenuService.lambdaQuery().eq(SysRoleMenu::getRoleId, roleId).list();
		if (CollUtil.isNotEmpty(roleMenuList)) {
			List<Long> menuIds = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(menuIds)) {
				sysMenus = lambdaQuery().in(SysMenu::getId, menuIds)
						.eq(null != menuType && menuType > 0, SysMenu::getType, menuType)
						.eq(SysMenu::getStatus, MenuStatusE.Normal.getId()).list();
			}
		}
		return sysMenus;
	}

	@Override
	@Transactional
	public Boolean deleteMenu(Long menuId) {
		sysRoleMenuService.lambdaUpdate().eq(SysRoleMenu::getMenuId, menuId).remove();
		removeById(menuId);
		return Boolean.TRUE;
	}

}
