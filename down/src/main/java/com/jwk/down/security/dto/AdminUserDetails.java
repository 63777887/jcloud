package com.jwk.down.security.dto;

import com.jwk.down.web.entity.SysUser;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


//把数据库信息转成security需要的
public class AdminUserDetails implements UserDetails {

  private SysUser sysUser;
  private List<ResourceConfigAttribute> resourceConfigAttributes;

  public AdminUserDetails() {
  }

  public AdminUserDetails(SysUser sysUser,
      List<ResourceConfigAttribute> resourceConfigAttributes) {
    this.sysUser = sysUser;
    this.resourceConfigAttributes = resourceConfigAttributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return resourceConfigAttributes;
  }


  @Override
  public String getPassword() {
    return sysUser.getPassword();
  }

  @Override
  public String getUsername() {
    return String.valueOf(sysUser.getId());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return sysUser.getStatus() == 1;
  }
}
