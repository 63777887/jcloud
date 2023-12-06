package com.jwk.common.security.support.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jwk.common.core.constant.JwkSecurityConstants;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jwk.common.log.enums.LogStatusE;
import com.jwk.common.log.enums.LogTypeE;
import com.jwk.common.log.event.SysLogEvent;
import com.jwk.common.log.utils.SysLogUtils;
import com.jwk.common.security.util.SecurityUtils;
import com.jwk.upms.base.dto.SysLogDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 登录成功事件
 * @date 2022/11/24
 */
@Slf4j
public class JwkAuthenticationSuccessEventHandler implements AuthenticationSuccessHandler {

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new JwkOAuth2AccessTokenResponseHttpMessageConverter();

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     */
    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
        Map<String, Object> map = accessTokenAuthentication.getAdditionalParameters();
        if (MapUtil.isNotEmpty(map)) {
            String userName = (String) map.get(JwkSecurityConstants.DETAILS_USER_NAME);
            if (log.isDebugEnabled()) {
                log.debug("用户：{} 登录成功", userName);
            }
            pushLoginSeccessLog();
        }

        // 输出token
        sendAccessTokenResponse(request, response, authentication);
    }

    private void pushLoginSeccessLog() {
        SysLogDto sysLogDto = SysLogUtils.getSysLog();
        sysLogDto.setLogType(LogTypeE.USER_LOGIN.getCode());
        sysLogDto.setLogTitle(LogTypeE.USER_LOGIN.getMsg());
        sysLogDto.setStatus(LogStatusE.SUCCESS_LOG.getCode());
        sysLogDto.setCreateBy(StrUtil.isNotBlank(SecurityUtils.getAuthentication().getName()) ? SecurityUtils.getAuthentication().getName() : "");
        SpringUtil.publishEvent(new SysLogEvent(sysLogDto));
    }

    private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException {

        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType()).scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (accessToken.getScopes().contains(OidcScopes.OPENID)) {
            builder.expiresIn(JwkSecurityConstants.ID_TOKEN_EXPIRE_AT);
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        // 无状态 注意删除 context 上下文的信息
        SecurityContextHolder.clearContext();
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }

}
