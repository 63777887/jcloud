package com.jwk.security.security.component;

import com.jwk.common.core.utils.JwkSpringUtil;
import com.jwk.security.security.service.JwkUserDetailsService;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 重写 {@link AuthenticationProvider} 自定义登录校验策略
 */
@Slf4j
public abstract class AbstractUserCustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			log.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException("Bad credentials");
		}

		// 此处已获得 客户端认证 获取对应 userDetailsService
		Authentication clientAuthentication = SecurityContextHolder.getContext().getAuthentication();
		String clientId = clientAuthentication.getName();

		Map details = (Map) authentication.getDetails();
		String grantType = details.get("grant_type").toString();

		Map<String, JwkUserDetailsService> userDetailsServiceMap = JwkSpringUtil
				.getBeansOfType(JwkUserDetailsService.class);
		Optional<JwkUserDetailsService> optional = userDetailsServiceMap.values().stream()
				.filter(service -> service.support(clientId)).filter(service -> service.supportGrantType(grantType)).max(Comparator.comparingInt(Ordered::getOrder));

		if (!optional.isPresent()) {
			throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
		}

		// 账号	用户名/手机号等
		String principal = authentication.getName();

		// 这里的code指的是校验码	密码/code等
		String code = authentication.getCredentials().toString();

		UserDetails userDetails = optional.get().loadUserByUsername(principal);

		if (!checkPrincipal(code, userDetails)) {
			throw new BadCredentialsException("Bad credentials");
		}

		JwkCustomAuthenticationToken token = new JwkCustomAuthenticationToken(userDetails);

		return token;
	}

	/**
	 * 用户信息校验
	 * @param code
	 * @param userDetails
	 * @return
	 */
	protected abstract boolean checkPrincipal(String code, UserDetails userDetails);


	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			this.logger.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}


}
