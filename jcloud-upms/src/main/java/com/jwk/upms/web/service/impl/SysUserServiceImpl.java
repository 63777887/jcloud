package com.jwk.upms.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysRole;
import com.jwk.upms.base.entity.SysRoleMenu;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.base.entity.SysUserRole;
import com.jwk.upms.web.dao.SysUserMapper;
import com.jwk.upms.dto.UserDto;
import com.jwk.upms.enums.MenuTypeE;
import com.jwk.upms.web.service.SysMenuService;
import com.jwk.upms.web.service.SysRoleMenuService;
import com.jwk.upms.web.service.SysRoleService;
import com.jwk.upms.web.service.SysUserRoleService;
import com.jwk.upms.web.service.SysUserService;
import com.jwk.upms.web.vo.UserVo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * `sys_user` 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private final SysUserRoleService sysUserRoleService;

	private final SysRoleMenuService sysRoleMenuService;

	private final SysMenuService sysMenuService;

	private final SysRoleService sysRoleService;

	@Override
	public UserInfo findUserById(Long id) {
		SysUser user = getById(id);
		if (BeanUtil.isEmpty(user)) {
			return null;
		}
		// 加载用户角色列表
		List<SysUserRole> sysUserRoles = sysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId, user.getId())
				.list();
		List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

		List<SysMenu> sysMenus = new ArrayList<>();
		for (Long roleId : roleIds) {
			// 通过用户角色列表加载用户的资源权限列表
			List<SysRoleMenu> sysRoleApis = sysRoleMenuService.lambdaQuery().eq(SysRoleMenu::getRoleId, roleId).list();
			List<Long> menuIds = sysRoleApis.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
			if (CollUtil.isNotEmpty(menuIds)) {
				sysMenus = sysMenuService.listByIds(menuIds);
			}
		}
		List<TreeNode<Long>> collect = sysMenus.stream().filter(menu -> MenuTypeE.MENU.getId().equals(menu.getType()))
				.filter(menu -> StrUtil.isNotBlank(menu.getPath())).map(getNodeFunction()).collect(Collectors.toList());

		List<SysMenu> buttons = sysMenus.stream().filter(menu -> MenuTypeE.BUTTON.getId().equals(menu.getType()))
				.collect(Collectors.toList());

		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(user);
		userInfo.setButtons(buttons);
		userInfo.setSysMenu(TreeUtil.build(collect, -1L));
		return userInfo;
	}

	@Override
	public Page<UserVo> getUserList(Page<SysUser> page, UserDto userDto) {
		Page<SysUser> userPage = lambdaQuery()
				.like(StrUtil.isNotBlank(userDto.getUsername()), SysUser::getUsername, userDto.getUsername())
				.page(page);
		List<UserVo> userVoList = userPage.getRecords().stream().map(t -> {
			UserVo userVo = Convert.convert(UserVo.class, t);
			userVo.setPhone(DesensitizedUtil.mobilePhone(userVo.getPhone()));
			return userVo;
		}).collect(Collectors.toList());

		Page<UserVo> userVoPage = Convert.convert(new TypeReference<Page<UserVo>>() {
		}, userPage);
		userVoPage.setRecords(userVoList);
		if (CollUtil.isNotEmpty(userVoList)) {
			userVoList.forEach(t -> {
				List<SysUserRole> list = sysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId, t.getId()).list();
				if (CollUtil.isNotEmpty(list)) {
					List<Long> roleIds = list.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
					List<SysRole> sysRoleList = sysRoleService.lambdaQuery().in(SysRole::getId, roleIds).list();
					t.setSysRoles(sysRoleList);
				}
			});
		}
		return userVoPage;
	}

	@Override
	public UserVo getUserById(Long id) {
		SysUser user = getById(id);
		UserVo userVo = Convert.convert(UserVo.class, user);
		List<SysUserRole> list = sysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId, userVo.getId()).list();
		if (CollUtil.isNotEmpty(list)) {
			List<Long> roleIds = list.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
			List<SysRole> sysRoleList = sysRoleService.lambdaQuery().in(SysRole::getId, roleIds).list();
			userVo.setSysRoles(sysRoleList);
		}
		return userVo;
	}

	@Override
	@Transactional
	public Boolean updateUser(UserDto userDto) {
		// 更新用户信息
		LambdaUpdateChainWrapper<SysUser> wrapper = lambdaUpdate()
				.set(StrUtil.isNotBlank(userDto.getUsername()), SysUser::getUsername, userDto.getUsername())
				.set(StrUtil.isNotBlank(userDto.getPhone()), SysUser::getPhone, userDto.getPhone())
				.set(StrUtil.isNotBlank(userDto.getNickname()), SysUser::getNickname, userDto.getNickname())
				.set(userDto.getStatus() != null && userDto.getStatus() > 0, SysUser::getStatus,
						userDto.getStatus().byteValue())
				.set(StrUtil.isNotBlank(userDto.getEmail()), SysUser::getEmail, userDto.getEmail());
		if (StrUtil.isNotBlank(userDto.getPassword())) {
			wrapper.set(SysUser::getPassword,
					PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userDto.getPassword()));
		}
		wrapper.eq(SysUser::getId, userDto.getId()).update();
		if (CollUtil.isNotEmpty(userDto.getRoles())) {
			// 更新角色信息
			sysUserRoleService.lambdaUpdate().eq(SysUserRole::getUserId, userDto.getId()).remove();

			List<SysUserRole> sysUserRoles = userDto.getRoles().stream().map(t -> {
				SysUserRole sysUserRole = new SysUserRole();
				sysUserRole.setUserId(userDto.getId());
				sysUserRole.setRoleId(t);
				return sysUserRole;
			}).collect(Collectors.toList());
			sysUserRoleService.saveBatch(sysUserRoles);
		}

		return Boolean.TRUE;
	}

	@Override
	public Boolean deleteUser(Long id) {
		removeById(id);
		sysUserRoleService.lambdaUpdate().eq(SysUserRole::getUserId, id).remove();
		return Boolean.TRUE;
	}

	@Override
	public Boolean deleteBatch(List<Long> userIds) {
		removeByIds(userIds);
		sysUserRoleService.lambdaUpdate().in(SysUserRole::getUserId, userIds).remove();
		return Boolean.TRUE;
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
			extra.put("path", menu.getPath());
			extra.put("menuName", menu.getMenuName());
			extra.put("sort", menu.getSort());
			extra.put("hidden", menu.getHidden());
			extra.put("tab", menu.getTab());
			node.setExtra(extra);
			return node;
		};
	}

}
