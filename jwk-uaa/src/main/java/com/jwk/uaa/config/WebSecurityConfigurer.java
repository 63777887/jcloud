/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jwk.uaa.config;

import com.jwk.security.security.component.JwkAuthProperties;
import com.jwk.security.security.conf.DynamicResourceFilter;
import com.jwk.security.security.conf.JwtAuthenticationFilter;
import com.jwk.security.security.handler.JwkAuthenticationFailHandler;
import com.jwk.security.security.handler.JwtForbiddenConfigHandler;
import com.jwk.uaa.comonpent.OauthCheckRequestService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author lengleng
 * @date 2019/2/1 认证相关配置
 */
@Primary
@Order(1)
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
  private JwkAuthProperties jwkAuthProperties;

	@Autowired
  private AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Autowired
  private OauthCheckRequestService oauthCheckRequestService;

	@Override
	@SneakyThrows
	protected void configure(HttpSecurity http) {
		http.csrf().disable()
        .sessionManagement()
        //  无状态模式
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // 自定义权限拒绝处理类
    // AuthenticationException指的是未登录状态下访问受保护资源
    // AccessDeniedException指的是登陆了但是由于权限不足(比如普通用户访问管理员界面）。
    http.exceptionHandling().authenticationEntryPoint(new JwkAuthenticationFailHandler())
        .accessDeniedHandler(new JwtForbiddenConfigHandler());

    String noauthUrl = jwkAuthProperties.getNoauthUrl();
    String[] noAuthUrlList = noauthUrl.split(",");
    http.authorizeRequests().antMatchers(noAuthUrlList).permitAll().anyRequest().authenticated();

    // 添加自定义的jwt过滤器
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
    jwtAuthenticationFilter.setCheckRequestService(oauthCheckRequestService);
    // 注入属性
    autowireCapableBeanFactory.autowireBean(jwtAuthenticationFilter);
    // 如果使用addFilter 则会抛异常，没有指定order
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    //添加自定义的权限过滤器
    DynamicResourceFilter dynamicResourceFilter = new DynamicResourceFilter();
    autowireCapableBeanFactory.autowireBean(dynamicResourceFilter);
    http.addFilterBefore(dynamicResourceFilter, FilterSecurityInterceptor.class);
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**");
	}

	@Bean
	@Override
	@SneakyThrows
	public AuthenticationManager authenticationManagerBean() {
		return super.authenticationManagerBean();
	}

//	@Bean
//	public AuthenticationFailureHandler authenticationFailureHandler() {
//		return new FormAuthenticationFailureHandler();
//	}

//	/**
//	 * 支持SSO 退出
//	 * @return LogoutSuccessHandler
//	 */
//	@Bean
//	public LogoutSuccessHandler logoutSuccessHandler() {
//		return new SsoLogoutSuccessHandler();
//	}

//	/**
//	 * https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-storage-updated
//	 * Encoded password does not look like BCrypt
//	 * @return PasswordEncoder
//	 */
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}

}
