package com.jwk.common.security.support.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.jwk.common.core.constant.JwkSecurityConstants;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jwk.common.core.constant.ResponseConstants;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.log.utils.SysLogUtils;
import com.jwk.upms.base.api.UpmsRemoteService;
import com.jwk.upms.base.dto.RemoveTokenDto;
import com.jwk.upms.base.entity.SysSetting;
import com.jwk.upms.base.utils.TokenUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
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
    private final UpmsRemoteService upmsRemoteService;
    private final RedisTemplate redisTemplate;

    public JwkAuthenticationSuccessEventHandler(UpmsRemoteService upmsRemoteService, RedisTemplate redisTemplate) {
        this.upmsRemoteService = upmsRemoteService;
        this.redisTemplate = redisTemplate;
    }

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
            Long userId = MapUtil.getLong(map, JwkSecurityConstants.DETAILS_USER_ID);
            String clientId = MapUtil.getStr(map, JwkSecurityConstants.CLIENT_ID);
            String userName = MapUtil.getStr(map, JwkSecurityConstants.DETAILS_USER_NAME);
            Long orgId = MapUtil.getLong(map, JwkSecurityConstants.DETAILS_ORGID);
            if (log.isDebugEnabled()) {
                log.debug("用户：{} 登录成功", userName);
            }
            SysLogUtils.pushLoginSuccessLog(userId, clientId);

            OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
            OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();

            String recordAccessTokenKey = TokenUtil.buildRecordKey(OAuth2ParameterNames.ACCESS_TOKEN, userId);
            String recordRefreshTokenKey = TokenUtil.buildRecordKey(OAuth2ParameterNames.REFRESH_TOKEN, userId);
            // 删除之前的记录
            upmsRemoteService.removeToken(RemoveTokenDto.builder().orgId(orgId).userId(userId).build());
            // 记录对应用户登陆的token
            redisTemplate.boundSetOps(recordAccessTokenKey).add(accessToken.getTokenValue());
            redisTemplate.boundSetOps(recordRefreshTokenKey).add(refreshToken.getTokenValue());
        }

        // 输出token
        sendAccessTokenResponse(request, response, authentication);
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
