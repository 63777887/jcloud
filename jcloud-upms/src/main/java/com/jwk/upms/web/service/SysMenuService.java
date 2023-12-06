package com.jwk.upms.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jwk.upms.base.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * `sys_menu` 服务类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
public interface SysMenuService extends IService<SysMenu> {

	List<SysMenu> getMenuListByRole(Long roleId, Integer menuType);

	Boolean deleteMenu(Long menuId);

}
