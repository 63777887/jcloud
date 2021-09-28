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

package com.jwk.security.feign;

import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.commons.security.AccessTokenContextRelay;
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 *  扩展OAuth2FeignRequestInterceptor
 */
@Slf4j
public class JwkFeignClientInterceptor extends OAuth2FeignRequestInterceptor {

	private final OAuth2ClientContext oAuth2ClientContext;

	private final AccessTokenContextRelay accessTokenContextRelay;

	/**
	 * Default constructor which uses the provided OAuth2ClientContext and Bearer tokens
	 * within Authorization header
	 * @param oAuth2ClientContext provided context
	 * @param resource type of resource to be accessed
	 * @param accessTokenContextRelay
	 */
	public JwkFeignClientInterceptor(OAuth2ClientContext oAuth2ClientContext, OAuth2ProtectedResourceDetails resource,
			AccessTokenContextRelay accessTokenContextRelay) {
		super(oAuth2ClientContext, resource);
		this.oAuth2ClientContext = oAuth2ClientContext;
		this.accessTokenContextRelay = accessTokenContextRelay;
	}

	/**
	 * Create a template with the header of provided name and extracted extract 1. 如果使用
	 * 非web 请求，header 区别 2. 根据authentication 还原请求token
	 * @param template
	 */
	@Override
	public void apply(RequestTemplate template) {
//		Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
//		if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
//			return;
//		}

		accessTokenContextRelay.copyToken();
		if (oAuth2ClientContext != null && oAuth2ClientContext.getAccessToken() != null) {
			super.apply(template);
		}
	}

}
