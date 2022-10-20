package com.jwk.common.security.security.grant;

import com.jwk.common.security.security.component.JwkCustomAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 自定义token
 */
public class PasswordAuthenticationToken extends JwkCustomAuthenticationToken {

	public PasswordAuthenticationToken(String phone, String code) {
		super(phone, code);
	}

	public PasswordAuthenticationToken(UserDetails sysUser) {
		super(sysUser);
	}

}
