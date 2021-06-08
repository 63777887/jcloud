package com.jwk.down.security.service.impl;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.jwk.down.security.dto.AdminUserDetails;
import com.jwk.down.security.dto.ResourceConfigAttribute;
import com.jwk.down.web.entity.SysApi;
import com.jwk.down.web.entity.SysRoleApi;
import com.jwk.down.web.entity.SysUser;
import com.jwk.down.web.entity.SysUserRole;
import com.jwk.down.web.service.SysApiService;
import com.jwk.down.web.service.SysRoleApiService;
import com.jwk.down.web.service.SysRoleService;
import com.jwk.down.web.service.SysUserRoleService;
import com.jwk.down.web.service.SysUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

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
        List<SysUser> list = new LambdaQueryChainWrapper<>(sysUserService.getBaseMapper())
            .eq(SysUser::getUsername, username).list();

        //加载用户角色列表
        List<SysUserRole> sysUserRoles = new LambdaQueryChainWrapper<>(userRoleService.getBaseMapper()).eq(
            SysUserRole::getUserId, list.get(0).getId()).list();
        List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId)
            .collect(Collectors.toList());

        List<SysApi> sysApis =new ArrayList<>();
        for(Long roleId : roleIds){
          //通过用户角色列表加载用户的资源权限列表
            List<SysRoleApi> sysRoleApis = new LambdaQueryChainWrapper<>(
                sysRoleApiService.getBaseMapper()).eq(
                SysRoleApi::getRoleId, roleId).list();
            List<Long> apiIds = sysRoleApis.stream().map(SysRoleApi::getApiId)
                .collect(Collectors.toList());
            sysApis = sysApiService.listByIds(apiIds);
        }

        List<ResourceConfigAttribute> configAttributes = sysApis.stream()
            .map(ResourceConfigAttribute::new)
            .collect(Collectors.toList());

        return new AdminUserDetails(list.get(0),configAttributes);
  }
}
