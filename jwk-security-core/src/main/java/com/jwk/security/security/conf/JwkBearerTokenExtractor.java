package com.jwk.security.security.conf;

import com.jwk.security.security.component.JwkAuthProperties;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 改造 {@link BearerTokenExtractor} 对公开权限的请求不进行校验
 *
 * @author caiqy
 * @date 2020.05.15
 */
public class JwkBearerTokenExtractor extends BearerTokenExtractor {

	private final PathMatcher pathMatcher;


	@Autowired
	private JwkAuthProperties jwkAuthProperties;

	public JwkBearerTokenExtractor() {
		this.pathMatcher = new AntPathMatcher();
	}

	@Override
	public Authentication extract(HttpServletRequest request) {

		String[] noAuthUrlList = jwkAuthProperties.getNoauthUrl().split(",");
		boolean match = Arrays.stream(noAuthUrlList)
				.anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));

		return match ? null : super.extract(request);
	}

}
