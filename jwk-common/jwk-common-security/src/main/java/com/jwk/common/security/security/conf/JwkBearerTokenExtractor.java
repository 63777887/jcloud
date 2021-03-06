package com.jwk.common.security.security.conf;


import com.jwk.common.security.security.component.JwkAuthProperties;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 改造 {@link BearerTokenExtractor} 对公开权限的请求不进行校验
 */
public class JwkBearerTokenExtractor extends BearerTokenExtractor {

	private final PathMatcher pathMatcher;

	private final JwkAuthProperties jwkAuthProperties;

	private final String ERROR = "/error";

	public JwkBearerTokenExtractor(
			JwkAuthProperties jwkAuthProperties) {
		this.jwkAuthProperties=jwkAuthProperties;
		this.pathMatcher = new AntPathMatcher();
	}

	@Override
	public Authentication extract(HttpServletRequest request) {

		String[] noAuthUrlList = jwkAuthProperties.getNoauthUrl().split(",");
		List<String> noAuthUrls = Arrays.stream(noAuthUrlList).collect(Collectors.toList());
		// 错误页面也不参与鉴权
		noAuthUrls.add(ERROR);
		boolean match = noAuthUrls.stream()
				.anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));

		return match ? null : super.extract(request);
	}

}
