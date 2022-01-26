package com.jwk.security.security.grant;

import com.jwk.security.security.component.JwkCustomAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 定义token
 */
public class PasswordAuthenticationToken extends JwkCustomAuthenticationToken {


	public PasswordAuthenticationToken(String phone, String code) {
		super(phone, code);
	}

	public PasswordAuthenticationToken(
			UserDetails sysUser) {
		super(sysUser);
	}
}
