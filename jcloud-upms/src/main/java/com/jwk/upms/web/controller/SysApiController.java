package com.jwk.upms.web.controller;

import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.model.RestResponse;
import com.jwk.upms.base.entity.SysApi;
import com.jwk.upms.web.service.SysApiService;
import com.jwk.upms.web.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
	private SysApiService sysApiService;

	/**
	 * 接口列表
	 */
	@GetMapping(value = "/list")
	public RestResponse list(@RequestParam(value = "url", required = false) String url) {
		List<SysApi> list = sysApiService.lambdaQuery().like(StrUtil.isNotBlank(url), SysApi::getUrl, url).list();
		return RestResponse.success(list);
	}

}
