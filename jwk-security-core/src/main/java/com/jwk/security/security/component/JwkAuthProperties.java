package com.jwk.security.security.component;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwk.auth")
@Data
public class JwkAuthProperties {

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

}
