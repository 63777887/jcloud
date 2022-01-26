package com.jwk.security.security.component;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 定义token
 */
public class JwkCustomAuthenticationToken extends AbstractAuthenticationToken {

	private Object principal;

	// 验证码/密码
	private String credentials;

	public JwkCustomAuthenticationToken(String principal, String credentials) {
		super(AuthorityUtils.NO_AUTHORITIES);
		this.principal = principal;
		this.credentials = credentials;
	}

	public JwkCustomAuthenticationToken(UserDetails sysUser) {
		super(sysUser.getAuthorities());
		super.setDetails(sysUser);
		this.principal = sysUser.getUsername();
		super.setAuthenticated(true); // 设置认证成功 必须
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

}
