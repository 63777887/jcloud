package com.jwk.uaa.config;

import cn.hutool.extra.spring.SpringUtil;
import com.jwk.common.security.support.component.JwkDaoAuthenticationProvider;
import com.jwk.common.security.support.properties.JwkAuthProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 服务安全相关配置
 * @date 2022/11/24
 */
@EnableWebSecurity(debug = true)
@Configuration
public class WebSecurityConfiguration {

	/**
	 * spring security 默认的安全策略
	 * @param http security注入点
	 * @return SecurityFilterChain
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		JwkAuthProperties properties = SpringUtil.getBean(JwkAuthProperties.class);
		http.authorizeRequests(authorizeRequests -> authorizeRequests.antMatchers("/token/*").permitAll()// 开放自定义的部分端点
				.anyRequest().authenticated()).headers().frameOptions().sameOrigin()// 避免iframe同源无法登录
				.and().apply(new FormIdentityLoginConfigurer()); // 表单登录个性化
		// 处理 UsernamePasswordAuthenticationToken
		JwkDaoAuthenticationProvider authenticationProvider = new JwkDaoAuthenticationProvider();
		authenticationProvider.setJwkAuthProperties(properties);
		http.authenticationProvider(authenticationProvider);
		return http.build();
	}

	/**
	 * 暴露静态资源
	 * <p>
	 * https://github.com/spring-projects/spring-security/issues/10938
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Order(0)
	SecurityFilterChain resources(HttpSecurity http) throws Exception {
		http.requestMatchers((matchers) -> matchers.antMatchers("/actuator/**", "/css/**", "/error"))
				.authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll()).requestCache().disable()
				.securityContext().disable().sessionManagement().disable();
		return http.build();
	}

}
