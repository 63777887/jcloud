package com.jwk.security.security.grant;

import com.jwk.security.security.service.impl.JwkUserDetailsService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 手机号码登录Provider
 * 重写 {@link AuthenticationProvider} 自定义登录校验策略,grant_type为phone是走该校验
 */
@Slf4j
public class PhoneAuthenticationProvider  extends AbstractUserDetailsAuthenticationProvider {

	@Setter
	private UserDetailsService userDetailsService;
	@Setter
	private PasswordEncoder passwordEncoder;


	/**
	 * 校验 请求信息userDetails
	 * @param userDetails 用户信息
	 * @param usernamePasswordAuthenticationToken 认证信息
	 * @throws AuthenticationException
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
			throws AuthenticationException {

		if (usernamePasswordAuthenticationToken.getCredentials() == null) {
			this.logger.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			log.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException("Bad credentials");
		}

		// 手机号
		String phone = authentication.getName();

		// 验证码/密码
		// 验证码模式 自己去实现验证码检验
		// 这里的code指的是密码
		String code = authentication.getCredentials().toString();

		UserDetails userDetails = ((JwkUserDetailsService) userDetailsService).loadUserByPhone(phone);

		String password = userDetails.getPassword();

		boolean matches = passwordEncoder.matches(code, password);
		if (!matches) {
			throw new BadCredentialsException("Bad credentials");
		}

		PhoneAuthenticationToken token = new PhoneAuthenticationToken(userDetails);

		token.setDetails(authentication.getDetails());

		return token;
	}

	@Override
	protected UserDetails retrieveUser(String s,
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
			throws AuthenticationException {
		return null;
	}


	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(PhoneAuthenticationToken.class);
	}
}