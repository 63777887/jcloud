package com.jwk.security.security.grant;

import com.jwk.security.security.component.JwkCustomAuthenticationProvider;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码认证Provider
 */
@Slf4j
public class PasswordAuthenticationProvider extends JwkCustomAuthenticationProvider {

	@Setter
	private PasswordEncoder passwordEncoder;

	@Override
	protected boolean checkPrincipal(String password, UserDetails userDetails) {
		return passwordEncoder.matches(password, userDetails.getPassword());
	}

	@Override
	protected UserDetails retrieveUser(String s,
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
			throws AuthenticationException {
		return null;
	}


	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(PasswordAuthenticationToken.class) ||
				authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}

}