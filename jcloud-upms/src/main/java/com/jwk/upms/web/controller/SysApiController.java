package com.jwk.upms.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.jwk.api.dto.SysApiDto;
import com.jwk.api.dto.SysUserDto;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.security.annotation.Inner;
import com.jwk.upms.web.entity.SysApi;
import com.jwk.upms.web.entity.SysRole;
import com.jwk.upms.web.entity.SysRoleApi;
import com.jwk.upms.web.entity.SysUserRole;
import com.jwk.upms.web.service.SysApiService;
import com.jwk.upms.web.service.SysRoleApiService;
import com.jwk.upms.web.service.SysRoleService;
import com.jwk.upms.web.service.SysUserRoleService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * `sys_api` 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/sysApi")
public class SysApiController {

	@Autowired
	private SysApiService sysApiService;

	@Autowired
	SysRoleService sysRoleService;

	@Autowired
	SysRoleApiService sysRoleApiService;

	/**
	 * 接口列表
	 */
	@GetMapping(value = "/list")
	public RestResponse list() {
		List<SysApi> list = sysApiService.list();
		return RestResponse.success(list);
	}

	/**
	 * 接口列表
	 */
	@Inner
	@GetMapping(value = "/loadUserAuthoritiesByRole")
	public RestResponse loadUserAuthoritiesByRole(String roleCode) {
		// 加载用户角色列表
		SysRole sysRole = sysRoleService.lambdaQuery().eq(SysRole::getCode, roleCode).one();
		if (BeanUtil.isEmpty(sysRole)){
			return RestResponse.success();
		}

		// 通过用户角色列表加载用户的资源权限列表
		List<SysRoleApi> sysRoleApis = sysRoleApiService.lambdaQuery().eq(SysRoleApi::getRoleId, sysRole.getId()).list();
		List<Long> apiIds = sysRoleApis.stream().map(SysRoleApi::getApiId).collect(Collectors.toList());
		List<SysApi> sysApis = sysApiService.listByIds(apiIds);
		List<SysApiDto> sysApiDtos = sysApis.stream().map(t -> Convert.convert(SysApiDto.class, t))
				.collect(Collectors.toList());
		return RestResponse.success(sysApiDtos);
	}

}
