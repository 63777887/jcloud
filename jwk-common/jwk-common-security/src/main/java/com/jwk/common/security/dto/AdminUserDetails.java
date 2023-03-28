package com.jwk.common.security.dto;

import com.jwk.upms.base.entity.SysUser;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 自定义UserDetails
 * @date 2022/6/11
 */
public class AdminUserDetails implements UserDetails, OAuth2AuthenticatedPrincipal {

	private final SysUser sysUser;

	private final List<ResourceConfigAttribute> authorities;

	private Map<String, Object> attributes;

	public AdminUserDetails(SysUser sysUser, List<ResourceConfigAttribute> authorities) {
		this.sysUser = sysUser;
		this.authorities = authorities;
	}

	public AdminUserDetails(Map<String, Object> attributes, List<ResourceConfigAttribute> authorities,
			String principalName) {
		this.authorities = authorities;
		this.attributes = attributes;
		this.sysUser = new SysUser();
		this.sysUser.setUsername(principalName);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return new HashMap<>();
	}

	/**
	 * 用户的权限集
	 * @return
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return sysUser.getPassword();
	}

	@Override
	public String getUsername() {
		return sysUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return sysUser.getStatus() == 1;
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

	@Override
	public String getName() {
		return this.getUsername();
	}

}
