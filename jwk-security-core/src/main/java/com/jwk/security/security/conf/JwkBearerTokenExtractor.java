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

package com.jwk.security.security.conf;

import cn.hutool.core.util.StrUtil;
import com.jwk.security.security.component.JwkAuthProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
