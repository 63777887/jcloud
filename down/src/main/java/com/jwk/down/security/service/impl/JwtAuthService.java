package com.jwk.down.security.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.jwk.down.constants.SecurityConstant;
import com.jwk.down.exception.ServiceException;
import com.jwk.down.enums.StatusE;
import com.jwk.down.security.service.TokenService;
import com.jwk.down.util.DateHelper;
import com.jwk.down.web.dto.RegisterReq;
import com.jwk.down.web.entity.SysUser;
import com.jwk.down.web.service.SysUserService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class JwtAuthService {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private TokenService tokenService;
  @Autowired
  private SysUserService sysUserService;

  @Autowired
  private PasswordEncoder passwordEncoder;


  /**
   * 登录认证换取JWT令牌
   *
   * @return JWT
   */
  public String login(String username,
      String password,
      Map<String, String> payloads) {
    try {
      // 构建未认证的Authentication对象
      UsernamePasswordAuthenticationToken upToken =
          new UsernamePasswordAuthenticationToken(username, password);

      // 让security去做登陆认证，AuthenticationManager会去调用
      // AbstractUserDetailsAuthenticationProvider.authenticate()方法
      // 他会去调用我们重写的loadUserByUsername()方法获取UserDetails对象，
      // 校验用户状态，同时校验用户密码与前端传入的是否一致
      Authentication authentication = authenticationManager.authenticate(upToken);

      // 把认证以后的信息放入security上下文
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (AuthenticationException e) {
      throw new ServiceException("登陆失败");
    }
      // 返回token
    return tokenService.generateToken(username);
  }


  public void register(@Valid RegisterReq user) {
    List<SysUser> allUser = sysUserService.list();
    allUser.forEach(t->{
      if (user.getPhone().equals(t.getPhone())){
        throw new ServiceException("该手机号已被注册！");
      }
      if (user.getUsername().equals(t.getUsername())){
        throw new ServiceException("该用户名已被注册！");
      }
    });
    SysUser sysUser = Convert.convert(SysUser.class, user);
    sysUser.setPassword(passwordEncoder.encode(user.getPassword()));
    sysUser.setStatus(StatusE.Normal.getId().byteValue());
    sysUser.setCreateTime(DateHelper.getLongDate(new Date()));
    sysUserService.save(sysUser);
  }

    public String refreshToken(String oldToken){
      String token = oldToken.substring(SecurityConstant.SCHEMA.length());
        if(tokenService.validateToken(token)){
            return tokenService.refreshToken(token);
        }
        return null;
    }

}
