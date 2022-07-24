package com.jwk.common.security;

import com.jwk.common.security.security.component.JwkAuthProperties;
import com.jwk.common.security.security.conf.DynamicAccessDecisionManager;
import com.jwk.common.security.security.conf.DynamicMetadataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 自动配置类
 */
@EnableConfigurationProperties(JwkAuthProperties.class)
@Import({DynamicAccessDecisionManager.class,DynamicMetadataSource.class})
public class SecurityCoreAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }



}
