package com.jwk.security.security.service.impl;


import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.UserInfo;
import com.jwk.security.security.service.JwkUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
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
    return grantType.equals("phone");
  }
}
