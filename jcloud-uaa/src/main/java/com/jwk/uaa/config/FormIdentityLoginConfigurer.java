package com.jwk.uaa.config;

import com.jwk.common.security.support.handler.FormAuthenticationFailureHandler;
import com.jwk.common.security.support.handler.SsoLogoutSuccessHandler;
import com.jwk.uaa.constant.JwkOAuth2Urls;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * @author Jiwk
 * @date 2022/11/24
 * @version 0.1.4
 * <p>
 * 登录登出控制
 */
@Slf4j
public final class FormIdentityLoginConfigurer
		extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

	@Override
	public void init(HttpSecurity http) throws Exception {
		http.formLogin(formLogin -> {
			formLogin.loginPage(JwkOAuth2Urls.LOGIN_PAGE);
			formLogin.defaultSuccessUrl(JwkOAuth2Urls.DEFAULT_SUCCESS_URL);
			formLogin.loginProcessingUrl(JwkOAuth2Urls.LOGIN_PROCESSING_URL);
			formLogin.failureHandler(new FormAuthenticationFailureHandler());
		}).logout() // SSO登出成功处理
				.logoutSuccessHandler(new SsoLogoutSuccessHandler()).deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).and().csrf().disable();

	}

}
