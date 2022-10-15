package com.jwk.common.security.security.service.impl;

import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.security.security.service.JwkUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 账号密码用户登陆service
 */
@Primary
public class JwkUserDetailsServiceImpl implements JwkUserDetailsService {

	@Autowired
	UpmsRemoteService upmsRemoteService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 加载基础用户信息
		// 加载基础用户信息
		UserInfo userInfo = upmsRemoteService.findUserByName(username).getData();
		return getUerDetail(userInfo);
	}

	@Override
	public boolean supportGrantType(String grantType) {
		return !"phone".equals(grantType);
	}

}
