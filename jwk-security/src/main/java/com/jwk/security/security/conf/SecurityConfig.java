package com.jwk.security.security.conf;

import com.jwk.security.security.handler.AuthenticationFailHandler;
import com.jwk.security.security.handler.JwtForbiddenConfigHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("#{'${noAuth.url:}'.split(',')}")
  private String[] noAuthUrlList;

  @Autowired
  private AutowireCapableBeanFactory autowireCapableBeanFactory;

  @Override
  public void configure(WebSecurity web) throws Exception {
    //白名单
//    web.ignoring().antMatchers("/admin/login").antMatchers(HttpMethod.OPTIONS, "/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable()
        .sessionManagement()
        //  无状态模式
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // 自定义权限拒绝处理类
    // AuthenticationException指的是未登录状态下访问受保护资源
    // AccessDeniedException指的是登陆了但是由于权限不足(比如普通用户访问管理员界面）。
    http.exceptionHandling().authenticationEntryPoint(new AuthenticationFailHandler())
        .accessDeniedHandler(new JwtForbiddenConfigHandler());

    http.authorizeRequests().antMatchers(noAuthUrlList).permitAll().anyRequest().authenticated();

    // 添加自定义的jwt过滤器
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
    // 注入属性
    autowireCapableBeanFactory.autowireBean(jwtAuthenticationFilter);
    // 如果使用addFilter 则会抛异常，没有指定order
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    //添加自定义的权限过滤器
    DynamicResourceFilter dynamicResourceFilter = new DynamicResourceFilter();
    autowireCapableBeanFactory.autowireBean(dynamicResourceFilter);
    http.addFilterBefore(dynamicResourceFilter, FilterSecurityInterceptor.class);

  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  @Bean
  // 登陆认证管理器
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
