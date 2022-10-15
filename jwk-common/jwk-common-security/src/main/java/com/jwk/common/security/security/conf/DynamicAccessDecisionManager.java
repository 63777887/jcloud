package com.jwk.common.security.security.conf;

import com.jwk.common.security.security.component.JwkAuthProperties;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 动态的权限验证
 */
public class DynamicAccessDecisionManager implements AccessDecisionManager {

	@Autowired
	JwkAuthProperties jwkAuthProperties;

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		Set<String> noAuthUrls = Arrays.stream(jwkAuthProperties.getNoAuthArray()).collect(Collectors.toSet());
		// 获取用户所有的权限
		Set<String> adminResources = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
		adminResources.addAll(noAuthUrls);
		// 用户权限和当前URL所需权限进行验证，符合则返回ture
		boolean isAuthorized = false;
		for (ConfigAttribute c : configAttributes) {
			// 鉴定admin是否具有访问地址所需要的权限
			if (adminResources.contains(c.getAttribute())) {
				isAuthorized = true;
				break;
			}
		}

		// 判断，符合则允许继续，否则跳出
		if (!isAuthorized) {
			throw new AccessDeniedException("没有访问权限");
		}
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
