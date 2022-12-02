package com.jwk.uaa.grant.email;

import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.security.support.service.JwkUserDetailsService;
import com.jwk.uaa.constant.JwkOAuth2ParameterNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 邮箱密码用户登陆service
 */
@Service
public class EmailUserDetailsServiceImpl implements JwkUserDetailsService {

	@Autowired
	UpmsRemoteService upmsRemoteService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// 加载基础用户信息
		UserInfo userInfo = upmsRemoteService.findUserByEmail(email).getData();
		return getUerDetail(userInfo);
	}

	@Override
	public boolean supportGrantType(String grantType) {
		return JwkOAuth2ParameterNames.EMAIL.equals(grantType);
	}
}