package com.jwk.common.security.security.dto;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * UserDetails
 */
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


  /**
   * 用户的权限集
   * @return
   */
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
    return String.valueOf(sysUser.getUsername());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return sysUser.getStatus()==1;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return sysUser.getStatus() == 1;
  }

  public SysUser getSysUser() {
    return sysUser;
  }
}
