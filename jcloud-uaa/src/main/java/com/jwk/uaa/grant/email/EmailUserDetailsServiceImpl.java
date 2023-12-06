package com.jwk.uaa.grant.email;

import com.jwk.upms.base.api.UpmsRemoteService;
import com.jwk.common.security.support.service.JwkUserDetailsService;
import com.jwk.uaa.constant.JwkOAuth2ParameterNames;
import com.jwk.upms.base.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 邮箱密码用户登陆service
 * @date 2022/6/11
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
