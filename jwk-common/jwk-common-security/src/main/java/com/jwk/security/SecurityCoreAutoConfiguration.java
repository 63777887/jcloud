package com.jwk.security;

import com.jwk.security.security.component.JwkAuthProperties;
import com.jwk.security.security.conf.DynamicAccessDecisionManager;
import com.jwk.security.security.conf.DynamicMetadataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableConfigurationProperties(JwkAuthProperties.class)
@Import({DynamicAccessDecisionManager.class,DynamicMetadataSource.class})
public class SecurityCoreAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }



}
