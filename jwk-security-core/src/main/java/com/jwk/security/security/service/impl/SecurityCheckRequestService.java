package com.jwk.security.security.service.impl;

import com.jwk.security.security.service.CheckRequestService;
import com.jwk.security.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityCheckRequestService implements CheckRequestService {

  @Autowired
  private TokenService tokenService;

  @Autowired
  private JwkUserDetailsService jwkUserDetailsService;

  @Override
  public UsernamePasswordAuthenticationToken checkToken(String token) {
    if (tokenService.validateToken(token)) {
      // 获取用户名
      String username = tokenService.parseSubject(token);
      //获取userDetails用户信息
      UserDetails userDetails = jwkUserDetailsService.loadUserByUsername(username);

      if (userDetails != null) {

        // token校验通过，设置身份认证信息
        // 两个参数构造方法表示身份未认证，三个参数构造方法表示身份已认证
        //usernamePasswordAuthenticationToken把getUserDetailsByToken获得的带有认证鉴权信息的userDetails交给security
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(), userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(userDetails);
        return usernamePasswordAuthenticationToken;
      }
    }
      return null;
  }
}
