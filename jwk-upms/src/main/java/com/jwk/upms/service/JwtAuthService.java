package com.jwk.upms.service;

import cn.hutool.core.convert.Convert;
import com.jwk.api.dto.RegisterReq;
import com.jwk.common.core.enums.StatusE;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.common.core.utils.DateHelper;
import com.jwk.upms.web.entity.SysUser;
import com.jwk.upms.web.service.SysUserService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class JwtAuthService {

  @Autowired
  private SysUserService sysUserService;

  private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();;


  public void register(RegisterReq user) {
    List<SysUser> allUser = sysUserService.list();
    allUser.forEach(t -> {
      if (user.getPhone().equals(t.getPhone())) {
        throw new ServiceException("该手机号已被注册！");
      }
      if (user.getUsername().equals(t.getUsername())) {
        throw new ServiceException("该用户名已被注册！");
      }
    });
    SysUser sysUser = Convert.convert(SysUser.class, user);
    sysUser.setPassword(passwordEncoder.encode(user.getPassword()));
    sysUser.setStatus(StatusE.Normal.getId().byteValue());
    sysUser.setCreateTime(DateHelper.getLongDate(new Date()));
    sysUserService.save(sysUser);
    // todo 给用户赋予角色和权限
  }

}
