package com.jwk.security;

import com.jwk.security.security.component.JwkAuthProperties;
import com.jwk.security.security.component.OauthCheckRequestService;
import com.jwk.security.security.conf.DynamicAccessDecisionManager;
import com.jwk.security.security.conf.DynamicMetadataSource;
import com.jwk.security.security.service.JwkUserDetailsService;
import com.jwk.security.security.service.impl.JwkPhoneUserDetailsServiceImpl;
import com.jwk.security.security.service.impl.JwkUserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties(JwkAuthProperties.class)
public class SecurityCoreConfig {

//  @Resource
//  private SqlSessionTemplate sqlSessionTemplate;

  public SecurityCoreConfig() {
  }

  @Bean
  public JwkUserDetailsService jwkUserDetailsService() {
    return new JwkUserDetailsServiceImpl();
  }

  @Bean
  public JwkUserDetailsService jwkPhoneUserDetailsService() {
    return new JwkPhoneUserDetailsServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public DynamicAccessDecisionManager dynamicAccessDecisionManager() {
    return new DynamicAccessDecisionManager();
  }

  @Bean
  @ConditionalOnMissingBean
  public DynamicMetadataSource dynamicMetadataSource() {
    return new DynamicMetadataSource();
  }

  @Bean
  @ConditionalOnMissingBean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  @ConditionalOnMissingBean
  public OauthCheckRequestService oauthCheckRequestService() {
    return new OauthCheckRequestService();
  }



}
