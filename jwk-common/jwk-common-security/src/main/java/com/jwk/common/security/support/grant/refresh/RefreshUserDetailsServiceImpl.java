package com.jwk.common.security.support.grant.refresh;

import cn.hutool.core.util.ObjectUtil;
import com.jwk.common.security.dto.AdminUserDetails;
import com.jwk.common.security.support.service.JwkUserDetailsService;
import com.jwk.upms.base.api.UpmsRemoteService;
import com.jwk.upms.base.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

import java.security.Principal;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 账号密码用户登陆service
 * @date 2022/6/11
 */
@Slf4j
@RequiredArgsConstructor
public class RefreshUserDetailsServiceImpl implements JwkUserDetailsService {

    private final OAuth2AuthorizationService authorizationService;

    @Override
    public UserDetails loadUserByUsername(String refreshToken) throws UsernameNotFoundException {
        // 加载基础用户信息
        refreshToken
                = refreshToken.replace("Bearer ", "");
        OAuth2Authorization authorization = authorizationService.findByToken(
                refreshToken, OAuth2TokenType.REFRESH_TOKEN);
        if (ObjectUtil.isEmpty(authorization)){
            throw new UsernameNotFoundException("invalid authenticationToken");
        }
        UsernamePasswordAuthenticationToken authenticationToken = authorization.getAttribute(Principal.class.getName());
        if (AdminUserDetails.class.isAssignableFrom(authenticationToken.getPrincipal().getClass())){
            return (AdminUserDetails) authenticationToken.getPrincipal();
        }
        log.error("authenticationToken principal convert adminUserDetails error");
        throw new UsernameNotFoundException("invalid authenticationToken");
    }


    @Override
    public boolean supportGrantType(String grantType) {
        return OAuth2ParameterNames.REFRESH_TOKEN.equals(grantType);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public boolean needPassword() {
        // 刷新token模式不需要密码校验
        return false;
    }
}
