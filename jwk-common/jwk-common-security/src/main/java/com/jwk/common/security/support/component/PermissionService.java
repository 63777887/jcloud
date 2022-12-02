package com.jwk.common.security.support.component;

import com.jwk.common.core.utils.WebUtils;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

/**
 * @author Jiwk
 * @date 2022/11/24
 * @version 0.1.4
 * <p>
 * 权限认证方案
 */
public class PermissionService {

	/**
	 * 判断用户是否有权限
	 * @return {boolean}
	 */
	public boolean hasPermission() {
		if (!WebUtils.getRequest().isPresent()) {
			return false;
		}
		// 获取当前url：url为我们的api权限，即访问当前接口所需要的权限
		String requestURI = WebUtils.getRequest().get().getRequestURI();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		// 获取当前用户拥有的权限
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		// 比较是否匹配
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
				.anyMatch(x -> antPathMatcher.match(x,requestURI));
	}

}
