package com.jwk.security.security.component;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwk.auth")
@Data
public class JwkAuthProperties implements InitializingBean {

    /**
     * 免鉴权路径
     */
    private String noauthUrl = "";
    /**
     * tokenHeader
     */
    private String tokenHeader="Authorization";
    /**
     * tokenSchema
     */
    private String tokenSchema="Bearer";
    /**
     * tokenSecretKey
     */
    private String secretKey="jiwk";
    /**
     * tokenExpireTime
     */
    private Long expireSec=86400000L;

    @Override
    public void afterPropertiesSet() throws Exception {
         String defaultNoAuthUrl = "/swagger-resources/**,/v2/api-docs/**,/webjars/**,"
            + "/doc.html,/**/*.css,/**/*.jpg/**/*.jpeg,/**/*.gif,/js/*.js,/**/*.png,/login.jsp";
        if (StrUtil.isBlank(noauthUrl)){
            noauthUrl = defaultNoAuthUrl;
        }else {
            noauthUrl = noauthUrl +","+defaultNoAuthUrl;
        }
    }
}
