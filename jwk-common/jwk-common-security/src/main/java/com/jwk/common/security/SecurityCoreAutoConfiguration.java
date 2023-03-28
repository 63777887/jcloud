package com.jwk.common.security;

import com.jwk.common.security.support.properties.JwkAuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 自动配置类
 * @date 2022/6/11
 */
@EnableConfigurationProperties(JwkAuthProperties.class)
public class SecurityCoreAutoConfiguration {

}
