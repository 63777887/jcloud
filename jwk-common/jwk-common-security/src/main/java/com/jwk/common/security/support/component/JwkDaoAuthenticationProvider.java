package com.jwk.common.security.support.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jwk.common.core.utils.WebUtils;
import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.security.support.properties.JwkAuthProperties;
import com.jwk.common.security.support.service.JwkUserDetailsService;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Jiwk
 * @date 2022/11/18
 * @version 0.1.4
 * <p>
 * UsernamePasswordAuthenticationToken 认证信息
 * 重写 {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}
 */
public class JwkDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private final static BasicAuthenticationConverter basicConvert = new BasicAuthenticationConverter();

	private PasswordEncoder passwordEncoder;

	/**
	 * The password used to perform {@link PasswordEncoder#matches(CharSequence, String)}
	 * on when the user is not found to avoid SEC-2056. This is necessary, because some
	 * {@link PasswordEncoder} implementations will short circuit if the password is not
	 * in a valid format.
	 */
	private volatile String userNotFoundEncodedPassword;

	private UserDetailsService userDetailsService;

	private UserDetailsPasswordService userDetailsPasswordService;

	private JwkAuthProperties jwkAuthProperties;

	public JwkDaoAuthenticationProvider() {
		setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		// 这边可以设置不需要校验密码的认证模式，如验证码模式

		if (authentication.getCredentials() == null) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Failed to authenticate since no credentials provided");
			}
			throw new BadCredentialsException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		String presentedPassword = authentication.getCredentials().toString();
		if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Failed to authenticate since password does not match stored value");
			}
			throw new BadCredentialsException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	@SneakyThrows
	@Override
	protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
		prepareTimingAttackProtection();
		HttpServletRequest request = WebUtils.getRequest().orElseThrow(
				(Supplier<Throwable>) () -> new InternalAuthenticationServiceException("web request is empty"));

		Map<String, String> paramMap = ServletUtil.getParamMap(request);
		String grantType = paramMap.get(OAuth2ParameterNames.GRANT_TYPE);
		String clientId = paramMap.get(OAuth2ParameterNames.CLIENT_ID);

		// 如果参数中没有，则获取 basic 中的client
		if (StrUtil.isBlank(clientId)) {
			clientId = basicConvert.convert(request).getName();
		}

		Map<String, JwkUserDetailsService> userDetailsServiceMap = SpringUtil
				.getBeansOfType(JwkUserDetailsService.class);

		String finalClientId = clientId;
		Optional<JwkUserDetailsService> optional = userDetailsServiceMap.values().stream()
				.filter(service -> service.support(finalClientId))
				.filter(service -> service.supportGrantType(grantType))
				.max(Comparator.comparingInt(Ordered::getOrder));

		if (!optional.isPresent()) {
			throw new InternalAuthenticationServiceException("No suitable userDetailsService provider");
		}

		try {
			UserDetails loadedUser = optional.get().loadUserByUsername(username);
			if (loadedUser == null) {
				throw new UsernameNotFoundException("UserDetailsService returned null, which is an interface contract violation");
			}
			return loadedUser;
		}
		catch (UsernameNotFoundException ex) {
			mitigateAgainstTimingAttack(authentication);
			throw ex;
		}
		catch (InternalAuthenticationServiceException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
	}

	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
		boolean upgradeEncoding = this.userDetailsPasswordService != null
				&& this.passwordEncoder.upgradeEncoding(user.getPassword());
		if (upgradeEncoding) {
			String presentedPassword = authentication.getCredentials().toString();
			String newPassword = this.passwordEncoder.encode(presentedPassword);
			user = this.userDetailsPasswordService.updatePassword(user, newPassword);
		}
		return super.createSuccessAuthentication(principal, authentication, user);
	}

	private void prepareTimingAttackProtection() {
		if (this.userNotFoundEncodedPassword == null) {
			this.userNotFoundEncodedPassword = this.passwordEncoder.encode(jwkAuthProperties.getDefaultPassword());
		}
	}

	private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
		if (authentication.getCredentials() != null) {
			String presentedPassword = authentication.getCredentials().toString();
			this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
		}
	}

	/**
	 * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
	 * not set, the password will be compared using
	 * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
	 * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
	 * types.
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		this.passwordEncoder = passwordEncoder;
		this.userNotFoundEncodedPassword = null;
	}

	protected PasswordEncoder getPasswordEncoder() {
		return this.passwordEncoder;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	protected UserDetailsService getUserDetailsService() {
		return this.userDetailsService;
	}

	public void setUserDetailsPasswordService(UserDetailsPasswordService userDetailsPasswordService) {
		this.userDetailsPasswordService = userDetailsPasswordService;
	}

	public JwkAuthProperties getJwkAuthProperties() {
		return jwkAuthProperties;
	}

	public void setJwkAuthProperties(
			JwkAuthProperties jwkAuthProperties) {
		this.jwkAuthProperties = jwkAuthProperties;
	}
}
