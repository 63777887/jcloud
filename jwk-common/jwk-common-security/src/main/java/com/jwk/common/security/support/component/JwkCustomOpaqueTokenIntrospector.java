package com.jwk.common.security.support.component;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.SysApiDto;
import com.jwk.common.security.dto.AdminUserDetails;
import com.jwk.common.security.dto.ResourceConfigAttribute;
import com.jwk.common.security.dto.SysApi;
import com.jwk.common.security.support.service.JwkUserDetailsService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * @author Jiwk
 * @date 2022/11/14
 * @version 0.1.4
 * <p>
 * 令牌认证
 */
@Slf4j
@RequiredArgsConstructor
public class JwkCustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private final OAuth2AuthorizationService authorizationService;

	/**
	 * Introspect and verify the given token, returning its attributes. Returning a Map is indicative that the token is valid.
	 * Params:
	 * token – the token to introspect
	 * Returns:
	 * the token's attributes
	 */
	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		OAuth2Authorization oldAuthorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (Objects.isNull(oldAuthorization)) {
			throw new InvalidBearerTokenException(token);
		}

		// 获取scope 所拥有的权限
		List<ResourceConfigAttribute> scopeAuthorities = getScopeAuthorities(oldAuthorization);

		// 若scope没有权限，直接返回
		if (CollUtil.isEmpty(scopeAuthorities)) {
			return new AdminUserDetails(oldAuthorization.getAttributes(),
					Collections.singletonList(new ResourceConfigAttribute(new SysApi())),
					oldAuthorization.getPrincipalName());
		}
		// 客户端模式默认返回
		if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(oldAuthorization.getAuthorizationGrantType())) {
			return new AdminUserDetails(oldAuthorization.getAttributes(), scopeAuthorities,
					oldAuthorization.getPrincipalName());
		}

		Map<String, JwkUserDetailsService> userDetailsServiceMap = SpringUtil
				.getBeansOfType(JwkUserDetailsService.class);

		Optional<JwkUserDetailsService> optional = userDetailsServiceMap.values().stream()
				.filter(service -> service.support(Objects.requireNonNull(oldAuthorization).getRegisteredClientId()))
				.filter(service -> service.supportGrantType(oldAuthorization.getAuthorizationGrantType().getValue()))
				.max(Comparator.comparingInt(Ordered::getOrder));

		UserDetails userDetails = null;
		try {
			Object principal = Objects.requireNonNull(oldAuthorization).getAttributes().get(Principal.class.getName());
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
			Object tokenPrincipal = usernamePasswordAuthenticationToken.getPrincipal();
			userDetails = optional.get().loadUserByUsername(((AdminUserDetails) tokenPrincipal).getUsername());

			// 获取scope和user的权限交集，即为当前用户可访问的权限
			List<ResourceConfigAttribute> userAuthoritys = userDetails.getAuthorities().stream()
					.map(t -> (ResourceConfigAttribute) t).collect(Collectors.toList());
			List<ResourceConfigAttribute> realAuthoritys = (List<ResourceConfigAttribute>) CollUtil.intersection(scopeAuthorities, userAuthoritys);
			AdminUserDetails adminUserDetails = (AdminUserDetails) userDetails;

			userDetails = new AdminUserDetails(adminUserDetails.getSysUser(),realAuthoritys);
		} catch (UsernameNotFoundException notFoundException) {
			if (log.isWarnEnabled()) {
				log.warn("用户不存在 {}", notFoundException.getLocalizedMessage());
			}
			throw notFoundException;
		}
		catch (Exception ex) {
			if (log.isErrorEnabled()) {
				log.error("资源服务器 introspect Token error {}", ex.getLocalizedMessage());
			}
		}
		return (AdminUserDetails) userDetails;
	}

	private List<ResourceConfigAttribute> getScopeAuthorities(OAuth2Authorization oldAuthorization) {
		List<ResourceConfigAttribute> scopeAuthorities = new ArrayList<>();
		UpmsRemoteService upmsRemoteService = SpringUtil.getBean(UpmsRemoteService.class);
		Object scope = oldAuthorization.getAttribute(
				"org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE");
		if (BeanUtil.isNotEmpty(scope)) {
			List<SysApiDto> sysApiDtos = upmsRemoteService.loadUserAuthoritiesByRole(((LinkedHashSet)scope).stream().findFirst().get().toString())
					.getData();
			if (CollUtil.isNotEmpty(sysApiDtos)){
				scopeAuthorities = sysApiDtos.stream().map(sysApiDto -> {
					SysApi sysApi = BeanUtil.copyProperties(sysApiDto, SysApi.class);
					return new ResourceConfigAttribute(sysApi);
				}).collect(Collectors.toList());
			}
		}
		return scopeAuthorities;
	}

}
