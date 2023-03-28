package com.jwk.upms.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.jwk.common.core.enums.StatusE;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.common.core.utils.DateHelper;
import com.jwk.upms.base.dto.RegisterReq;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysApi;
import com.jwk.upms.base.entity.SysRoleApi;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.base.entity.SysUserRole;
import com.jwk.upms.web.service.AuthService;
import com.jwk.upms.web.service.SysApiService;
import com.jwk.upms.web.service.SysRoleApiService;
import com.jwk.upms.web.service.SysRoleService;
import com.jwk.upms.web.service.SysUserRoleService;
import com.jwk.upms.web.service.SysUserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 远程认证
 * @date 2022/6/11
 */
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	SysUserService sysUserService;

	@Autowired
	SysRoleService sysRoleService;

	@Autowired
	SysUserRoleService userRoleService;

	@Autowired
	SysRoleApiService sysRoleApiService;

	@Autowired
	SysApiService sysApiService;

	private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	public void register(RegisterReq user) {
		List<SysUser> allUser = sysUserService.list();
		allUser.forEach(t -> {
			if (user.getPhone().equals(t.getPhone())) {
				throw new ServiceException("该手机号已被注册！");
			}
			if (user.getUsername().equals(t.getUsername())) {
				throw new ServiceException("该用户名已被注册！");
			}
		});
		SysUser sysUser = Convert.convert(SysUser.class, user);
		sysUser.setPassword(passwordEncoder.encode(user.getPassword()));
		sysUser.setStatus(StatusE.Normal.getId().byteValue());
		sysUser.setCreateTime(DateHelper.getLongDate(new Date()));
		sysUserService.save(sysUser);
		// todo 给用户赋予角色和权限
	}

	@Override
	public UserInfo findUserByName(String name) {
		SysUser user = sysUserService.lambdaQuery().eq(SysUser::getUsername, name).one();
		UserInfo userInfo = getUserInfo(user);
		return userInfo;
	}

	private UserInfo getUserInfo(SysUser user) {
		if (BeanUtil.isEmpty(user)) {
			return null;
		}
		// 加载用户角色列表
		List<SysUserRole> sysUserRoles = userRoleService.lambdaQuery().eq(SysUserRole::getUserId, user.getId()).list();
		List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

		List<SysApi> sysApis = new ArrayList<>();
		for (Long roleId : roleIds) {
			// 通过用户角色列表加载用户的资源权限列表
			List<SysRoleApi> sysRoleApis = sysRoleApiService.lambdaQuery().eq(SysRoleApi::getRoleId, roleId).list();
			List<Long> apiIds = sysRoleApis.stream().map(SysRoleApi::getApiId).collect(Collectors.toList());
			sysApis = sysApiService.listByIds(apiIds);
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(user);
		userInfo.setSysApis(sysApis);
		return userInfo;
	}

	@Override
	public UserInfo findUserByPhone(String phone) {
		SysUser user = sysUserService.lambdaQuery().eq(SysUser::getPhone, phone).one();
		return getUserInfo(user);
	}

	@Override
	public List<SysApi> resourceList() {
		List<SysApi> list = sysApiService.lambdaQuery().eq(SysApi::getStatus, StatusE.Normal.getId()).list();
		return list.stream().map(t -> Convert.convert(SysApi.class, t)).collect(Collectors.toList());
	}

	@Override
	public Integer testSeata() {
		SysUser sysUser = new SysUser();
		sysUser.setOrgId(100L);
		sysUser.setId(4L);
		boolean update = sysUserService.lambdaUpdate().update(sysUser);
		return update ? 1 : 0;
	}

	@Override
	public UserInfo findUserByEmail(String mail) {
		SysUser user = sysUserService.lambdaQuery().eq(SysUser::getEmail, mail).one();
		return getUserInfo(user);
	}

}
