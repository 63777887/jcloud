package com.jwk.security;

import com.jwk.security.security.component.JwkAuthProperties;
import com.jwk.security.security.conf.DynamicAccessDecisionManager;
import com.jwk.security.security.conf.DynamicMetadataSource;
import com.jwk.security.security.conf.JwkBearerTokenExtractor;
import com.jwk.security.security.service.TokenService;
import com.jwk.security.security.service.impl.JwkUserDetailsService;
import com.jwk.security.security.service.impl.JwtAuthService;
import com.jwk.security.security.service.impl.TokenServiceImpl;
import com.jwk.security.web.controller.JwtAuthController;
import com.jwk.security.web.controller.SysApiController;
import com.jwk.security.web.controller.SysRoleController;
import com.jwk.security.web.controller.SysUserController;
import com.jwk.security.web.dao.SysApiCategoryMapper;
import com.jwk.security.web.dao.SysApiMapper;
import com.jwk.security.web.dao.SysMenuMapper;
import com.jwk.security.web.dao.SysOrgMapper;
import com.jwk.security.web.dao.SysRoleApiMapper;
import com.jwk.security.web.dao.SysRoleMapper;
import com.jwk.security.web.dao.SysRoleMenuMapper;
import com.jwk.security.web.dao.SysUserMapper;
import com.jwk.security.web.dao.SysUserRoleMapper;
import com.jwk.security.web.service.SysApiCategoryService;
import com.jwk.security.web.service.SysApiService;
import com.jwk.security.web.service.SysMenuService;
import com.jwk.security.web.service.SysOrgService;
import com.jwk.security.web.service.SysRoleApiService;
import com.jwk.security.web.service.SysRoleMenuService;
import com.jwk.security.web.service.SysRoleService;
import com.jwk.security.web.service.SysUserRoleService;
import com.jwk.security.web.service.SysUserService;
import com.jwk.security.web.service.impl.SysApiCategoryServiceImpl;
import com.jwk.security.web.service.impl.SysApiServiceImpl;
import com.jwk.security.web.service.impl.SysMenuServiceImpl;
import com.jwk.security.web.service.impl.SysOrgServiceImpl;
import com.jwk.security.web.service.impl.SysRoleApiServiceImpl;
import com.jwk.security.web.service.impl.SysRoleMenuServiceImpl;
import com.jwk.security.web.service.impl.SysRoleServiceImpl;
import com.jwk.security.web.service.impl.SysUserRoleServiceImpl;
import com.jwk.security.web.service.impl.SysUserServiceImpl;
import javax.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties(JwkAuthProperties.class)
public class SecurityCoreConfig {

  @Resource
  private SqlSessionTemplate sqlSessionTemplate;

  public SecurityCoreConfig() {
  }

  @Bean
  @ConditionalOnMissingBean
  public SysApiCategoryMapper sysApiCategoryMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysApiCategoryMapper.class);
    return this.sqlSessionTemplate.getMapper(SysApiCategoryMapper.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SysApiMapper sysApiMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysApiMapper.class);
    return this.sqlSessionTemplate.getMapper(SysApiMapper.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SysMenuMapper sysMenuMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysMenuMapper.class);
    return this.sqlSessionTemplate.getMapper(SysMenuMapper.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SysOrgMapper sysOrgMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysOrgMapper.class);
    return this.sqlSessionTemplate.getMapper(SysOrgMapper.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SysRoleApiMapper sysRoleApiMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysRoleApiMapper.class);
    return this.sqlSessionTemplate.getMapper(SysRoleApiMapper.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SysRoleMapper sysRoleMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysRoleMapper.class);
    return this.sqlSessionTemplate.getMapper(SysRoleMapper.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SysRoleMenuMapper sysRoleMenuMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysRoleMenuMapper.class);
    return this.sqlSessionTemplate.getMapper(SysRoleMenuMapper.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SysUserMapper sysUserMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysUserMapper.class);
    return this.sqlSessionTemplate.getMapper(SysUserMapper.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SysUserRoleMapper sysUserRoleMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysUserRoleMapper.class);
    return this.sqlSessionTemplate.getMapper(SysUserRoleMapper.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SysApiCategoryService sysApiCategoryService() {
    return new SysApiCategoryServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysApiService sysApiService() {
    return new SysApiServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysMenuService sysMenuService() {
    return new SysMenuServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysOrgService sysOrgService() {
    return new SysOrgServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysRoleApiService sysRoleApiService() {
    return new SysRoleApiServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysRoleMenuService sysRoleMenuService() {
    return new SysRoleMenuServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysRoleService sysRoleService() {
    return new SysRoleServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysUserRoleService sysUserRoleService() {
    return new SysUserRoleServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysUserService sysUserService() {
    return new SysUserServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public TokenService tokenService() {
    return new TokenServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public JwkUserDetailsService myUserDetailsService() {
    return new JwkUserDetailsService();
  }

  @Bean
  @ConditionalOnMissingBean
  public JwtAuthService jwtAuthService() {
    return new JwtAuthService();
  }

  @Bean
  @ConditionalOnMissingBean
  public JwtAuthController jwtAuthController() {
    return new JwtAuthController();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysUserController sysUserController() {
    return new SysUserController();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysApiController sysApiController() {
    return new SysApiController();
  }

  @Bean
  @ConditionalOnMissingBean
  public SysRoleController sysRoleController() {
    return new SysRoleController();
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

}
