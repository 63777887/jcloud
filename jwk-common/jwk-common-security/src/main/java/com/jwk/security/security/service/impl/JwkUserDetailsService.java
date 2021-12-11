package com.jwk.security.security.service.impl;


import com.jwk.security.security.dto.AdminUserDetails;
import com.jwk.security.security.dto.ResourceConfigAttribute;
import com.jwk.security.web.entity.SysApi;
import com.jwk.security.web.entity.SysRoleApi;
import com.jwk.security.web.entity.SysUser;
import com.jwk.security.web.entity.SysUserRole;
import com.jwk.security.web.service.SysApiService;
import com.jwk.security.web.service.SysRoleApiService;
import com.jwk.security.web.service.SysRoleService;
import com.jwk.security.web.service.SysUserRoleService;
import com.jwk.security.web.service.SysUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwkUserDetailsService implements UserDetailsService {

  @Autowired
  SysUserService sysUserService;
  @Autowired
  SysRoleService sysRoleService;
  @Autowired
  SysUserRoleService userRoleService;
  @Autowired
  SysRoleApiService sysRoleApiService;
  @Autowired
  SysApiService sysApiService;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
//        加载基础用户信息

    SysUser user = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();
    //加载用户角色列表

    List<ResourceConfigAttribute> configAttributes = getUerDetail(
        user);

    return new AdminUserDetails(user, configAttributes);
  }

  private List<ResourceConfigAttribute> getUerDetail(SysUser user) {
    List<SysUserRole> sysUserRoles = userRoleService.lambdaQuery()
        .eq(SysUserRole::getUserId, user.getId()).list();
    List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId)
        .collect(Collectors.toList());

    List<SysApi> sysApis = new ArrayList<>();
    for (Long roleId : roleIds) {
      //通过用户角色列表加载用户的资源权限列表
      List<SysRoleApi> sysRoleApis = sysRoleApiService.lambdaQuery()
          .eq(SysRoleApi::getRoleId, roleId)
          .list();
      List<Long> apiIds = sysRoleApis.stream().map(SysRoleApi::getApiId)
          .collect(Collectors.toList());
      sysApis = sysApiService.listByIds(apiIds);
    }

    List<ResourceConfigAttribute> configAttributes = sysApis.stream()
        .map(ResourceConfigAttribute::new)
        .collect(Collectors.toList());
    return configAttributes;
  }

  /**
   * 手机号码登录
   *
   * @param phone 手机号码
   * @return 用户信息
   */
  public UserDetails loadUserByPhone(String phone) {
    SysUser user = sysUserService.lambdaQuery().eq(SysUser::getPhone, phone).one();
    List<ResourceConfigAttribute> uerDetail = getUerDetail(user);
    return new AdminUserDetails(user, uerDetail);
  }
}
