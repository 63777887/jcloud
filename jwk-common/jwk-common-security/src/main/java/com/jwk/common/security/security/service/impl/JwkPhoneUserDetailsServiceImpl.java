package com.jwk.common.security.security.service.impl;


import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.security.security.service.JwkUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 手机登陆用户service
 */
public class JwkPhoneUserDetailsServiceImpl implements JwkUserDetailsService {

  @Autowired
  UpmsRemoteService upmsRemoteService;

  @Override
  public UserDetails loadUserByUsername(String phone)
      throws UsernameNotFoundException {
//        加载基础用户信息
    UserInfo userInfo = upmsRemoteService.findUserByPhone(phone).getData();
    return getUerDetail(userInfo);
  }

  @Override
  public boolean supportGrantType(String grantType) {
    return "phone".equals(grantType);
  }
}
