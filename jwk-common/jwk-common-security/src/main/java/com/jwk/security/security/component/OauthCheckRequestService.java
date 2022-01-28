package com.jwk.security.security.component;

import cn.hutool.core.convert.Convert;
import com.jwk.api.api.UaaRemoteService;
import com.jwk.api.dto.SysApiDto;
import com.jwk.api.dto.SysUserDto;
import com.jwk.api.dto.UserInfo;
import com.jwk.api.exception.InternalApiException;
import com.jwk.security.security.dto.AdminUserDetails;
import com.jwk.security.security.dto.ResourceConfigAttribute;
import com.jwk.security.security.dto.SysApi;
import com.jwk.security.security.dto.SysUser;
import com.jwk.security.security.service.CheckRequestService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OauthCheckRequestService implements CheckRequestService {

  @Autowired
  UaaRemoteService uaaRemoteService;

  @Override
  public UsernamePasswordAuthenticationToken checkToken(String token) {

    UserInfo userInfo = null;
    try {
      userInfo = uaaRemoteService.checkToken(token);
    } catch (InternalApiException e) {
      e.printStackTrace();
    }
    //获取userDetails用户信息
    AdminUserDetails userDetails = getUerDetail(userInfo);
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
      return null;
  }

  private AdminUserDetails getUerDetail(UserInfo user) {
    if (null == user){
      throw new UsernameNotFoundException("用户不存在");
    }
    SysUserDto sysUserdto = user.getSysUser();
    List<SysApiDto> sysApiDtos = user.getSysApis();

    SysUser sysUser = Convert.convert(SysUser.class, sysUserdto);
    List<SysApi> sysApis = sysApiDtos.stream().map(t -> Convert.convert(SysApi.class, t))
        .collect(Collectors.toList());

    List<ResourceConfigAttribute> configAttributes = sysApis.stream()
        .map(ResourceConfigAttribute::new)
        .collect(Collectors.toList());
    return new AdminUserDetails(sysUser,configAttributes);
  }
}
