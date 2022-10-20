package com.jwk.common.security.security.service;

import cn.hutool.core.convert.Convert;
import com.jwk.api.dto.SysApiDto;
import com.jwk.api.dto.SysUserDto;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.security.security.dto.AdminUserDetails;
import com.jwk.common.security.security.dto.ResourceConfigAttribute;
import com.jwk.common.security.security.dto.SysApi;
import com.jwk.common.security.security.dto.SysUser;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.Ordered;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 获取用户信息
 */
public interface JwkUserDetailsService extends UserDetailsService, Ordered {

	/**
	 * 是否支持此客户端校验
	 * @param clientId 目标客户端
	 * @return true/false
	 */
	default boolean support(String clientId) {
		return true;
	}

	/**
	 * 是否支持此登录方式
	 * @param grantType 目标客户端
	 * @return true/false
	 */
	default boolean supportGrantType(String grantType) {
		return true;
	}

	/**
	 * 排序值 默认取最大的
	 * @return 排序值
	 */
	@Override
	default int getOrder() {
		return 0;
	}

	/**
	 * 转换用户详情
	 * @param user
	 * @return
	 */
	default AdminUserDetails getUerDetail(UserInfo user) {
		if (null == user) {
			throw new UsernameNotFoundException("用户不存在");
		}
		SysUserDto sysUserdto = user.getSysUser();
		List<SysApiDto> sysApiDtos = user.getSysApis();

		SysUser sysUser = Convert.convert(SysUser.class, sysUserdto);
		List<SysApi> sysApis = sysApiDtos.stream().map(t -> Convert.convert(SysApi.class, t))
				.collect(Collectors.toList());

		List<ResourceConfigAttribute> configAttributes = sysApis.stream().map(ResourceConfigAttribute::new)
				.collect(Collectors.toList());
		return new AdminUserDetails(sysUser, configAttributes);
	}

}
