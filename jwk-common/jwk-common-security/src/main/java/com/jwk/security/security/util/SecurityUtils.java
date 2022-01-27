package com.jwk.security.security.util;

import com.jwk.security.security.dto.AdminUserDetails;
import com.jwk.security.security.dto.ResourceConfigAttribute;
import com.jwk.security.security.dto.SysUser;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * 安全工具类
 */
@UtilityClass
public class SecurityUtils {

  /**
   * 获取Authentication
   */
  public Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2Authentication){
      return ((OAuth2Authentication) authentication).getUserAuthentication();
    }
    return authentication;
  }

  /**
   * 获取用户
   */
  private SysUser getUser(Authentication authentication) {
    Object principal = authentication.getDetails();
    if (principal instanceof SysUser) {
      return (SysUser) principal;
    }
    if (principal instanceof AdminUserDetails) {
      return ((AdminUserDetails) principal).getSysUser();
    }
    return null;
  }

  /**
   * 获取用户
   */
  public SysUser getUser() {
    Authentication authentication = getAuthentication();
    if (authentication == null) {
      return null;
    }
    return getUser(authentication);
  }


}
