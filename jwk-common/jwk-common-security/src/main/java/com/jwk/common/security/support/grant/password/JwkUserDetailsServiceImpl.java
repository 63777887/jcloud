package com.jwk.common.security.support.grant.password;

import com.jwk.common.security.support.feign.api.UpmsRemoteService;
import com.jwk.common.security.support.service.JwkUserDetailsService;
import com.jwk.upms.base.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 账号密码用户登陆service
 */
public class JwkUserDetailsServiceImpl implements JwkUserDetailsService {

	@Autowired
	UpmsRemoteService upmsRemoteService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 加载基础用户信息
		UserInfo userInfo = upmsRemoteService.findUserByName(username).getData();
		return getUerDetail(userInfo);
	}

}
