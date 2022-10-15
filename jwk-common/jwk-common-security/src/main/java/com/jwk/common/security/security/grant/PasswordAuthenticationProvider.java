package com.jwk.common.security.security.grant;

import com.jwk.common.security.security.component.AbstractUserCustomAuthenticationProvider;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 密码认证Provider
 */
@Slf4j
public class PasswordAuthenticationProvider extends AbstractUserCustomAuthenticationProvider {

	@Setter
	private PasswordEncoder passwordEncoder;

	@Override
	protected boolean checkPrincipal(String password, UserDetails userDetails) {
		return passwordEncoder.matches(password, userDetails.getPassword());
	}

	@Override
	protected UserDetails retrieveUser(String s,
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(PasswordAuthenticationToken.class)
				|| authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}

}