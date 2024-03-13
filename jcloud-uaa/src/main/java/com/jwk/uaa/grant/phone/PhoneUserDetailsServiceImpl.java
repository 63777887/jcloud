package com.jwk.uaa.grant.phone;

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
 * 手机登陆用户service
 * @date 2022/6/11
 */
@Service
public class PhoneUserDetailsServiceImpl implements JwkUserDetailsService {

	@Autowired
	UpmsRemoteService upmsRemoteService;

	@Override
	public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
		// 加载基础用户信息
		UserInfo userInfo = upmsRemoteService.findUserByPhone(phone).getData();
		return getUerDetail(userInfo);
	}

	@Override
	public boolean supportGrantType(String grantType) {
		return JwkOAuth2ParameterNames.PHONE.equals(grantType);
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
