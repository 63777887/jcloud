package com.jwk.upms.feign.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.SysApiDto;
import com.jwk.api.dto.SysUserDto;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.core.enums.StatusE;
import com.jwk.common.core.model.InnerResponse;
import com.jwk.upms.web.entity.SysApi;
import com.jwk.upms.web.entity.SysRoleApi;
import com.jwk.upms.web.entity.SysUser;
import com.jwk.upms.web.entity.SysUserRole;
import com.jwk.upms.web.service.SysApiService;
import com.jwk.upms.web.service.SysRoleApiService;
import com.jwk.upms.web.service.SysRoleService;
import com.jwk.upms.web.service.SysUserRoleService;
import com.jwk.upms.web.service.SysUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpmsRemoteServiceImpl implements UpmsRemoteService {

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
  public InnerResponse<UserInfo> findUserByName(String name) {
    SysUser user = sysUserService.lambdaQuery().eq(SysUser::getUsername, name).one();
    UserInfo userInfo = getUserInfo(user);
    return InnerResponse.success(userInfo);
  }

  private UserInfo getUserInfo(SysUser user) {
    if (BeanUtil.isEmpty(user)){
      return null;
    }
    //加载用户角色列表
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

    UserInfo userInfo = new UserInfo();
    SysUserDto userDto = Convert.convert(SysUserDto.class, user);
    List<SysApiDto> sysApiDtos = sysApis.stream().map(t -> Convert.convert(SysApiDto.class, t))
        .collect(Collectors.toList());
    userInfo.setSysUser(userDto);
    userInfo.setSysApis(sysApiDtos);
    return userInfo;
  }

  @Override
  public InnerResponse<UserInfo> findUserByPhone(String phone) {
    SysUser user = sysUserService.lambdaQuery().eq(SysUser::getPhone, phone).one();
    UserInfo userInfo = getUserInfo(user);
    return InnerResponse.success(userInfo);
  }

  @Override
  public InnerResponse<List<SysApiDto>> resourceList() {
    List<SysApi> list = sysApiService.lambdaQuery().eq(SysApi::getStatus, StatusE.Normal.getId())
        .list();
    return InnerResponse.success(list.stream().map(t->Convert.convert(SysApiDto.class,t)).collect(Collectors.toList()));
  }
}
