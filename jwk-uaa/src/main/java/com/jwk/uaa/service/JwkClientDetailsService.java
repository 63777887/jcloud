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

package com.jwk.uaa.service;

import javax.sql.DataSource;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;

/**
 * see JdbcClientDetailsService
 */

@Component
public class JwkClientDetailsService extends JdbcClientDetailsService {

	public JwkClientDetailsService(DataSource dataSource) {
		super(dataSource);
	}

//	/**
//	 * 重写原生方法支持redis缓存
//	 * @param clientId
//	 * @return
//	 */
//	@Override
//	@SneakyThrows
//	@Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
//	public ClientDetails loadClientByClientId(String clientId) {
//		return super.loadClientByClientId(clientId);
//	}

}
