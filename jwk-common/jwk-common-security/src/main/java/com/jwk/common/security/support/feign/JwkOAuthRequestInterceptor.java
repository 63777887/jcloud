package com.jwk.common.security.support.feign;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.constant.JwkSecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 重新 OAuth2FeignRequestInterceptor 递解决feign 传递token 为空问题
 * @date 2022/11/13
 */
@Slf4j
@RequiredArgsConstructor
public class JwkOAuthRequestInterceptor implements RequestInterceptor {

	private final BearerTokenResolver tokenResolver;

	@Override
	public void apply(RequestTemplate template) {
		Collection<String> fromHeader = template.headers().get(JwkSecurityConstants.FROM);
		// 带from 请求直接跳过
		if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(JwkSecurityConstants.FROM_IN)) {
			return;
		}

		HttpServletRequest request = Optional
				.of(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
						.getRequest())
				.get();
		// 避免请求参数的 query token 无法传递
		String token = tokenResolver.resolve(request);
		if (StrUtil.isBlank(token)) {
			return;
		}
		template.header(HttpHeaders.AUTHORIZATION,
				String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), token));

	}

}
