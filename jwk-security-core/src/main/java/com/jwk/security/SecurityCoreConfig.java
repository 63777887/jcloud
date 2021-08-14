//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jwk.security;

import com.jwk.security.security.conf.DynamicAccessDecisionManager;
import com.jwk.security.security.conf.DynamicMetadataSource;
import com.jwk.security.security.service.TokenService;
import com.jwk.security.security.service.impl.JwtAuthService;
import com.jwk.security.security.service.impl.MyUserDetailsService;
import com.jwk.security.security.service.impl.TokenServiceImpl;
import com.jwk.security.web.controller.JwtAuthController;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityCoreConfig {

  @Resource
  private SqlSessionTemplate sqlSessionTemplate;

  public SecurityCoreConfig() {
  }

  @Bean
  public SysApiCategoryMapper sysApiCategoryMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysApiCategoryMapper.class);
    return this.sqlSessionTemplate.getMapper(SysApiCategoryMapper.class);
  }

  @Bean
  public SysApiMapper sysApiMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysApiMapper.class);
    return this.sqlSessionTemplate.getMapper(SysApiMapper.class);
  }

  @Bean
  public SysMenuMapper sysMenuMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysMenuMapper.class);
    return this.sqlSessionTemplate.getMapper(SysMenuMapper.class);
  }

  @Bean
  public SysOrgMapper sysOrgMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysOrgMapper.class);
    return this.sqlSessionTemplate.getMapper(SysOrgMapper.class);
  }

  @Bean
  public SysRoleApiMapper sysRoleApiMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysRoleApiMapper.class);
    return this.sqlSessionTemplate.getMapper(SysRoleApiMapper.class);
  }

  @Bean
  public SysRoleMapper sysRoleMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysRoleMapper.class);
    return this.sqlSessionTemplate.getMapper(SysRoleMapper.class);
  }

  @Bean
  public SysRoleMenuMapper sysRoleMenuMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysRoleMenuMapper.class);
    return this.sqlSessionTemplate.getMapper(SysRoleMenuMapper.class);
  }

  @Bean
  public SysUserMapper sysUserMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysUserMapper.class);
    return this.sqlSessionTemplate.getMapper(SysUserMapper.class);
  }

  @Bean
  public SysUserRoleMapper sysUserRoleMapper() throws Exception {
    this.sqlSessionTemplate.getConfiguration().addMapper(SysUserRoleMapper.class);
    return this.sqlSessionTemplate.getMapper(SysUserRoleMapper.class);
  }

  @Bean
  public SysApiCategoryService sysApiCategoryService() {
    return new SysApiCategoryServiceImpl();
  }

  @Bean
  public SysApiService sysApiService() {
    return new SysApiServiceImpl();
  }

  @Bean
  public SysMenuService sysMenuService() {
    return new SysMenuServiceImpl();
  }

  @Bean
  public SysOrgService sysOrgService() {
    return new SysOrgServiceImpl();
  }

  @Bean
  public SysRoleApiService sysRoleApiService() {
    return new SysRoleApiServiceImpl();
  }

  @Bean
  public SysRoleMenuService sysRoleMenuService() {
    return new SysRoleMenuServiceImpl();
  }

  @Bean
  public SysRoleService sysRoleService() {
    return new SysRoleServiceImpl();
  }

  @Bean
  public SysUserRoleService sysUserRoleService() {
    return new SysUserRoleServiceImpl();
  }

  @Bean
  public SysUserService sysUserService() {
    return new SysUserServiceImpl();
  }

  @Bean
  public TokenService tokenService() {
    return new TokenServiceImpl();
  }

  @Bean
  public MyUserDetailsService myUserDetailsService() {
    return new MyUserDetailsService();
  }

  @Bean
  public JwtAuthService jwtAuthService() {
    return new JwtAuthService();
  }

  @Bean
  public JwtAuthController jwtAuthController() {
    return new JwtAuthController();
  }

  @Bean
  public SysUserController sysUserController() {
    return new SysUserController();
  }

  @Bean
  public DynamicAccessDecisionManager dynamicAccessDecisionManager() {
    return new DynamicAccessDecisionManager();
  }

  @Bean
  public DynamicMetadataSource dynamicMetadataSource() {
    return new DynamicMetadataSource();
  }

}
