package com.jwk.security.security.component;

import com.jwk.security.constants.JwkSecurityConstants;
import com.jwk.security.security.dto.AdminUserDetails;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

/**
 * 根据自定义token 的结果转化用户信息
 */
public class JwkUserCreateAuthenticationConverter implements UserAuthenticationConverter {

	private static final String N_A = "N/A";



	/**
	 * Extract information about the user to be used in an access token (i.e. for resource
	 * servers).
	 * @param authentication an authentication representing a user
	 * @return a map of key values representing the unique information about the user
	 */
	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(USERNAME, authentication.getName());
		AdminUserDetails principal = (AdminUserDetails) authentication.getPrincipal();
		response.put(JwkSecurityConstants.DETAILS_USER_ID, principal.getSysUser().getId());
		response.put(JwkSecurityConstants.DETAILS_PHONE, principal.getSysUser().getPhone());
		response.put(JwkSecurityConstants.DETAILS_ORG_ID, principal.getSysUser().getOrgId());
		response.put(JwkSecurityConstants.DETAILS_EMAIL, principal.getSysUser().getEmail());
		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		}
		return response;
	}

	/**
	 * Inverse of {@link #convertUserAuthentication(Authentication)}. Extracts an
	 * Authentication from a map.
	 * @param map a map of user information
	 * @return an Authentication representing the user or null if there is none
	 */
	@Override
	public Authentication extractAuthentication(Map<String, ?> map) {
		if (map.containsKey(USERNAME)) {
			Collection<? extends GrantedAuthority> authorities = getAuthorities(map);

			// 此处map加载的信息为生成tokenEnhancer时添加的信息
			String username = (String) map.get(JwkSecurityConstants.DETAILS_USERNAME);
			return new UsernamePasswordAuthenticationToken(username, N_A, authorities);
		}
		return null;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
		Object authorities = map.get(AUTHORITIES);
		if (authorities instanceof String) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList(
					StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		return AuthorityUtils.NO_AUTHORITIES;
	}

}
