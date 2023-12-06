package com.jwk.upms.web.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.utils.DateUtil;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.dto.MenuDto;
import com.jwk.upms.enums.ErrorCodeStatusE;
import com.jwk.upms.enums.MenuStatusE;
import com.jwk.upms.enums.MenuTypeE;
import com.jwk.upms.web.service.SysMenuService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
		List<TreeNode<Long>> collect = all.stream().map(getNodeFunction()).collect(Collectors.toList());
		return RestResponse.success(TreeUtil.build(collect, -1L));
	}

	@GetMapping("/allMenu")
	public RestResponse getAllMenu() {
		List<SysMenu> all = sysMenuService.lambdaQuery().eq(SysMenu::getStatus, MenuStatusE.Normal.getId())
				.eq(SysMenu::getType, MenuTypeE.MENU.getId()).list();

		List<TreeNode<Long>> collect = all.stream().map(getNodeFunction()).collect(Collectors.toList());
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

	@GetMapping("/getMenuListByRole/{roleId}")
	public RestResponse getAllMenu(@PathVariable Long roleId, Integer menuType) {
		return RestResponse.success(sysMenuService.getMenuListByRole(roleId, menuType));
	}

	@NotNull
	private Function<SysMenu, TreeNode<Long>> getNodeFunction() {
		return menu -> {
			TreeNode<Long> node = new TreeNode<>();
			node.setId(menu.getId());
			node.setName(menu.getMenuName());
			node.setParentId(menu.getParentId());
			node.setWeight(menu.getSort());
			// 扩展属性
			Map<String, Object> extra = new HashMap<>();
			extra.put("icon", menu.getIcon());
			extra.put("menuName", menu.getMenuName());
			extra.put("path", menu.getPath());
			extra.put("type", menu.getType());
			extra.put("permission", menu.getPermission());
			extra.put("status", menu.getStatus());
			extra.put("sort", menu.getSort());
			extra.put("hidden", menu.getHidden());
			extra.put("tab", menu.getTab());
			extra.put("createTime", menu.getCreateTime());
			extra.put("updateTime", menu.getUpdateTime());
			node.setExtra(extra);
			return node;
		};
	}

}
