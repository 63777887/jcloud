package com.jwk.common.security.support.service;

import com.jwk.common.security.dto.AdminUserDetails;
import com.jwk.common.security.dto.ResourceConfigAttribute;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysUser;
import org.springframework.core.Ordered;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 获取用户信息
 * @date 2022/6/11
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
	 * 是否需要密码校验
	 * @return true/false
	 */
	default boolean needPassword() {
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
		SysUser sysUser = user.getSysUser();
		List<SysMenu> sysMenus = user.getButtons();

		List<ResourceConfigAttribute> configAttributes = sysMenus.stream().map(ResourceConfigAttribute::new)
				.collect(Collectors.toList());
		return new AdminUserDetails(sysUser, configAttributes);
	}

}
