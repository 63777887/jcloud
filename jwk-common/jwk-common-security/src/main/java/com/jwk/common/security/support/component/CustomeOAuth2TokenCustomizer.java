package com.jwk.common.security.support.component;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.security.dto.AdminUserDetails;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 增强token，加入user信息
 * @date 2022/11/24
 */
public class CustomeOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

	/**
	 * Customize the OAuth 2.0 Token attributes.
	 * @param context the context containing the OAuth 2.0 Token attributes
	 */
	@Override
	public void customize(OAuth2TokenClaimsContext context) {
		OAuth2TokenClaimsSet.Builder claims = context.getClaims();
		claims.claim(JwkSecurityConstants.DETAILS_LICENSE, JwkSecurityConstants.PROJECT_LICENSE);
		String clientId = context.getAuthorizationGrant().getName();
		claims.claim(JwkSecurityConstants.CLIENT_ID, clientId);
		// 客户端模式不返回具体用户信息
		if (JwkSecurityConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
			return;
		}

		AdminUserDetails adminUserDetails = (AdminUserDetails) context.getPrincipal().getPrincipal();
		claims.claim(JwkSecurityConstants.DETAILS_USER_NAME, adminUserDetails.getSysUser().getUsername());
		claims.claim(JwkSecurityConstants.DETAILS_USER_ID, adminUserDetails.getSysUser().getId());
		if (adminUserDetails.getSysUser().getOrgId()!=null) {
			claims.claim(JwkSecurityConstants.DETAILS_ORGID, adminUserDetails.getSysUser().getOrgId());
		}
	}

}
