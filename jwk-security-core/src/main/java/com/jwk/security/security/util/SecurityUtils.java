/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jwk.security.security.util;

import com.jwk.security.security.dto.AdminUserDetails;
import com.jwk.security.security.dto.ResourceConfigAttribute;
import com.jwk.security.web.entity.SysUser;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {

  /**
   * 获取Authentication
   */
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  /**
   * 获取用户
   */
  private SysUser getUser(Authentication authentication) {
    Object principal = authentication.getDetails();
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

  /**
   * 获取用户角色信息
   *
   * @return 角色集合
   */
  public List<Long> getRoles() {
    Authentication authentication = getAuthentication();
    if (authentication == null) {
      return null;
    }
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Object principal = authentication.getPrincipal();
    Object details = authentication.getDetails();
    return authorities.stream().map(auth -> {
      if (auth instanceof ResourceConfigAttribute) {
        return ((ResourceConfigAttribute) auth).getSysApi().getId();
      }
      return null;
    }).collect(Collectors.toList());
  }

}
