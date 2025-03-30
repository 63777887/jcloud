package com.jwk.upms.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.model.R;

import com.jwk.common.core.utils.AssertUtil;
import com.jwk.common.security.annotation.Inner;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.web.service.SmsService;
import com.jwk.upms.web.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/sms")
@Slf4j
public class SmsController {

	@Autowired
	SysUserService sysUserService;

	@Autowired
	SmsService smsService;

	/**
	 * 接口列表
	 */
	@PostMapping(value = "/sendCode")
	@Inner
	public R sendCode(String phone) {
		AssertUtil.isTrue(StrUtil.isNotBlank(phone), "手机号不能为空");
		SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getPhone, phone).one();
		if (BeanUtil.isEmpty(sysUser)) {
			return R.error("用户不存在");
		}
		return R.ok(smsService.sendCode(phone));
	}

}
