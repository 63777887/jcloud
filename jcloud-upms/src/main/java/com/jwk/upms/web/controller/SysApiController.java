package com.jwk.upms.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.security.annotation.Inner;
import com.jwk.upms.base.entity.SysApi;
import com.jwk.upms.base.entity.SysRole;
import com.jwk.upms.base.entity.SysRoleApi;
import com.jwk.upms.web.service.SysApiService;
import com.jwk.upms.web.service.SysRoleApiService;
import com.jwk.upms.web.service.SysRoleService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	SysRoleService sysRoleService;

	@Autowired
	SysRoleApiService sysRoleApiService;

	@Autowired
	private SysApiService sysApiService;

	/**
	 * 接口列表
	 */
	@GetMapping(value = "/list")
	public RestResponse list(@RequestParam(value = "url", required = false) String url) {
		List<SysApi> list = sysApiService.lambdaQuery().like(StrUtil.isNotBlank(url), SysApi::getUrl, url).list();
		return RestResponse.success(list);
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
		List<SysRoleApi> sysRoleApis = sysRoleApiService.lambdaQuery().in(SysRoleApi::getRoleId, sysRoleIds).list();
		List<Long> apiIds = sysRoleApis.stream().map(SysRoleApi::getApiId).collect(Collectors.toList());
		List<SysApi> sysApis = sysApiService.listByIds(apiIds);
		List<SysApi> sysApiDtos = sysApis.stream().map(t -> Convert.convert(SysApi.class, t))
				.collect(Collectors.toList());
		return RestResponse.success(sysApiDtos);
	}

}
