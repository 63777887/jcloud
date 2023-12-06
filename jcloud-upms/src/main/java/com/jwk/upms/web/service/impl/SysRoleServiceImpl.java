package com.jwk.upms.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.upms.base.entity.SysApi;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysRole;
import com.jwk.upms.base.entity.SysRoleApi;
import com.jwk.upms.base.entity.SysRoleMenu;
import com.jwk.upms.base.entity.SysUserRole;
import com.jwk.upms.web.dao.SysRoleMapper;
import com.jwk.upms.dto.RoleDto;
import com.jwk.upms.dto.SetRoleMenuDto;
import com.jwk.upms.enums.ErrorCodeStatusE;
import com.jwk.upms.enums.MenuTypeE;
import com.jwk.upms.enums.RoleStatusE;
import com.jwk.upms.web.service.SysApiService;
import com.jwk.upms.web.service.SysMenuService;
import com.jwk.upms.web.service.SysRoleApiService;
import com.jwk.upms.web.service.SysRoleMenuService;
import com.jwk.upms.web.service.SysRoleService;
import com.jwk.upms.web.service.SysUserRoleService;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * `sys_role` 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	private final SysRoleMenuService sysRoleMenuService;

	private final SysUserRoleService sysUserRoleService;

	private final SysMenuService sysMenuService;

	private final SysApiService sysApiService;

	private final SysRoleApiService sysRoleApiService;

	@Override
	public Page<SysRole> getRoleList(Page<SysRole> page, RoleDto roleDto) {
		return lambdaQuery()
				.like(StrUtil.isNotBlank(roleDto.getRoleName()), SysRole::getRoleName, roleDto.getRoleName())
				.ne(SysRole::getStatus, RoleStatusE.Delete.getId()).page(page);
	}

	@Override
	public Boolean saveRole(RoleDto roleDto) {
		SysRole sysRole = Convert.convert(SysRole.class, roleDto);
		sysRole.setStatus(RoleStatusE.Normal.getId());
		save(sysRole);
		return Boolean.TRUE;
	}

	@Override
	public SysRole getRoleById(Long id) {
		return getById(id);
	}

	@Override
	public Boolean updateRole(RoleDto roleDto) {
		if (roleDto.getId() <= 0) {
			throw new ServiceException(ErrorCodeStatusE.ROLE_ID_EMPTY.getCode(),
					ErrorCodeStatusE.ROLE_ID_EMPTY.getMsg());
		}
		// 更新用户信息
		lambdaUpdate().set(StrUtil.isNotBlank(roleDto.getRoleName()), SysRole::getRoleName, roleDto.getRoleName())
				.set(StrUtil.isNotBlank(roleDto.getRoleDesc()), SysRole::getRoleDesc, roleDto.getRoleDesc())
				.set(StrUtil.isNotBlank(roleDto.getCode()), SysRole::getCode, roleDto.getCode())
				.eq(SysRole::getId, roleDto.getId()).update();
		return Boolean.TRUE;
	}

	@Override
	@Transactional
	public Boolean deleteRole(Long roleId) {
		removeById(roleId);
		sysRoleMenuService.lambdaUpdate().eq(SysRoleMenu::getRoleId, roleId).remove();
		sysUserRoleService.lambdaUpdate().eq(SysUserRole::getRoleId, roleId).remove();
		return Boolean.TRUE;
	}

	@Override
	@Transactional
	public Boolean setRoleMenu(SetRoleMenuDto setRoleMenuDto) {
		sysRoleMenuService.setRoleMenu(setRoleMenuDto);
		// 如果是按钮权限，还需要同步更新下接口权限
		if (setRoleMenuDto.getMenuType().equals(MenuTypeE.BUTTON.getId().intValue())) {
			List<SysMenu> sysMenus = sysMenuService.listByIds(setRoleMenuDto.getMenuIds());
			HashSet<String> apis = new HashSet<>();
			sysMenus.forEach(t -> {
				if (StrUtil.isNotBlank(t.getPath())) {
					apis.add(t.getPath());
				}
			});
			if (CollUtil.isNotEmpty(apis)) {
				List<SysApi> sysApis = sysApiService.lambdaQuery().in(SysApi::getUrl, apis).list();
				List<SysRoleApi> roleApiList = sysApis.stream().map(t -> {
					SysRoleApi sysRoleApi = new SysRoleApi();
					sysRoleApi.setRoleId(setRoleMenuDto.getRoleId());
					sysRoleApi.setApiId(t.getId());
					return sysRoleApi;
				}).collect(Collectors.toList());
				if (CollUtil.isNotEmpty(roleApiList)) {
					sysRoleApiService.lambdaUpdate().eq(SysRoleApi::getRoleId, setRoleMenuDto.getRoleId()).remove();
					sysRoleApiService.saveBatch(roleApiList);
				}
			}
		}
		return Boolean.TRUE;
	}

}
