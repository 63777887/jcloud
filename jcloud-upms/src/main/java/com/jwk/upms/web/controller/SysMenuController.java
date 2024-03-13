package com.jwk.upms.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.utils.DateUtil;
import com.jwk.common.security.annotation.Inner;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysRole;
import com.jwk.upms.base.entity.SysRoleMenu;
import com.jwk.upms.dto.MenuDto;
import com.jwk.upms.enums.ErrorCodeStatusE;
import com.jwk.upms.enums.MenuStatusE;
import com.jwk.upms.enums.MenuTypeE;
import com.jwk.upms.web.service.SysMenuService;
import com.jwk.upms.web.service.SysRoleMenuService;
import com.jwk.upms.web.service.SysRoleService;
import com.jwk.upms.utils.MenuUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * `sys_menu` 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/sysMenu")
@RequiredArgsConstructor
public class SysMenuController {

	private final SysMenuService sysMenuService;

	private final SysRoleService sysRoleService;

	private final SysRoleMenuService sysRoleMenuService;

	@GetMapping("/tree")
	public RestResponse tree(@RequestParam(name = "menuName", required = false) String menuName,
			@RequestParam(name = "menuType", required = false) Integer menuType) {
		List<SysMenu> all = sysMenuService.lambdaQuery()
				.like(StrUtil.isNotBlank(menuName), SysMenu::getMenuName, menuName)
				.eq(SysMenu::getStatus, MenuStatusE.Normal.getId())
				.eq(null != menuType && menuType > 0, SysMenu::getType, menuType).list();
		if (StrUtil.isNotBlank(menuName)) {
			all.forEach(t -> t.setParentId(-1L));
		}
		List<TreeNode<Long>> collect = all.stream().map(MenuUtil.getNodeFunction()).collect(Collectors.toList());
		return RestResponse.success(TreeUtil.build(collect, -1L));
	}

	@GetMapping("/allMenu")
	public RestResponse getAllMenu() {
		List<SysMenu> all = sysMenuService.lambdaQuery().eq(SysMenu::getStatus, MenuStatusE.Normal.getId())
				.eq(SysMenu::getType, MenuTypeE.MENU.getId()).list();

		List<TreeNode<Long>> collect = all.stream().map(MenuUtil.getNodeFunction()).collect(Collectors.toList());
		return RestResponse.success(TreeUtil.build(collect, 0L));
	}

	@PostMapping("/add")
	@PreAuthorize("@pms.hasPermission()")
	public RestResponse add(@RequestBody @Valid MenuDto menuDto) {
		SysMenu sysMenu = Convert.convert(SysMenu.class, menuDto);
		return RestResponse.success(sysMenuService.save(sysMenu));
	}

	/**
	 * 根据ID删除菜单信息
	 */
	@DeleteMapping(value = "/delete/{id}")
	@PreAuthorize("@pms.hasPermission()")
	public RestResponse delete(@PathVariable Long id) {
		return RestResponse.success(sysMenuService.deleteMenu(id));
	}

	/**
	 * 根据ID查找用户信息
	 */
	@GetMapping(value = "/{id}")
	public RestResponse getMenuById(@PathVariable Long id) {
		return RestResponse.success(sysMenuService.getById(id));
	}

	@PostMapping("/update")
	@PreAuthorize("@pms.hasPermission()")
	public RestResponse update(@RequestBody MenuDto menuDto) {
		if (null == menuDto.getId() || menuDto.getId() <= 0) {
			throw new ServiceException(ErrorCodeStatusE.MENU_ID_EMPTY.getCode(),
					ErrorCodeStatusE.MENU_ID_EMPTY.getMsg());
		}
		return RestResponse.success(sysMenuService.lambdaUpdate()
				.set(StrUtil.isNotBlank(menuDto.getMenuName()), SysMenu::getMenuName, menuDto.getMenuName())
				.set(StrUtil.isNotBlank(menuDto.getPermission()), SysMenu::getPermission, menuDto.getPermission())
				.set(StrUtil.isNotBlank(menuDto.getPath()), SysMenu::getPath, menuDto.getPath())
				.set(StrUtil.isNotBlank(menuDto.getIcon()), SysMenu::getIcon, menuDto.getIcon())
				.set(null != menuDto.getParentId() && menuDto.getParentId() > 0, SysMenu::getParentId,
						menuDto.getParentId())
				.set(null != menuDto.getHidden() && menuDto.getHidden() > 0, SysMenu::getHidden, menuDto.getHidden())
				.set(null != menuDto.getTab() && menuDto.getTab() > 0, SysMenu::getTab, menuDto.getTab())
				.set(null != menuDto.getSort() && menuDto.getSort() > 0, SysMenu::getSort, menuDto.getSort())
				.set(null != menuDto.getStatus() && menuDto.getStatus() > 0, SysMenu::getStatus, menuDto.getStatus())
				.set(SysMenu::getUpdateTime, DateUtil.nowDate()).eq(SysMenu::getId, menuDto.getId()).update());
	}

	/**
	 * 接口列表
	 */
	@Inner
	@GetMapping(value = "/loadUserAuthoritiesByRole")
	public RestResponse loadUserAuthoritiesByRole(@RequestParam("roleCodeList") List<String> roleCodeList) {
		// 加载用户角色列表
		List<SysRole> sysRoleList = sysRoleService.lambdaQuery().in(SysRole::getCode, roleCodeList).list();
		if (CollUtil.isEmpty(sysRoleList)) {
			return RestResponse.success();
		}
		List<Long> sysRoleIds = sysRoleList.stream().map(SysRole::getId).collect(Collectors.toList());
		// 通过用户角色列表加载用户的资源权限列表
		List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.lambdaQuery().in(SysRoleMenu::getRoleId, sysRoleIds).list();
		if (CollUtil.isEmpty(sysRoleMenuList)) {
			return RestResponse.success();
		}
		List<Long> menuIds = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
		List<SysMenu> sysApis = sysMenuService.lambdaQuery().in(SysMenu::getId,menuIds).list();
		return RestResponse.success(sysApis);
	}

	@GetMapping("/getMenuListByRole/{roleId}")
	public RestResponse getAllMenu(@PathVariable Long roleId, Integer menuType) {
		return RestResponse.success(sysMenuService.getMenuListByRole(roleId, menuType));
	}

}
