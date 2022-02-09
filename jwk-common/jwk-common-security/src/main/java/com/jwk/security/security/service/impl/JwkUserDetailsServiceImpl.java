package com.jwk.security.security.service.impl;


import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.UserInfo;
import com.jwk.security.security.service.JwkUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwkUserDetailsServiceImpl implements JwkUserDetailsService {

  @Autowired
  UpmsRemoteService upmsRemoteService;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
//        加载基础用户信息
//        加载基础用户信息
    UserInfo userInfo = upmsRemoteService.findUserByName(username).getData();
    return getUerDetail(userInfo);
  }

  @Override
  public boolean supportGrantType(String grantType) {
    return !grantType.equals("phone");
  }
}
