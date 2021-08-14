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

package com.jwk.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.jwk.gateway.config.GatewayConfigProperties;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author lengleng
 * @date 2019 /2/1 密码解密工具类
 */
@Slf4j
@RequiredArgsConstructor
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory {

  private static final String PASSWORD = "password";

  private static final String KEY_ALGORITHM = "AES";
  private static final String OAUTH_TOKEN_URL = "/admin/login";

  private final GatewayConfigProperties configProperties;

  @Override
  public GatewayFilter apply(Object config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();

      // 不是登录请求，直接向下执行
      if (!CharSequenceUtil.containsAnyIgnoreCase(request.getURI().getPath(),
          OAUTH_TOKEN_URL)) {
        return chain.filter(exchange);
      }

      // jwk:获取到登陆请求的URL和参数
      URI uri = exchange.getRequest().getURI();
      String queryParam = uri.getRawQuery();
      Map<String, String> paramMap = HttpUtil.decodeParamMap(queryParam, CharsetUtil.CHARSET_UTF_8);

      // jwk:密码解码后重新加入参数列表
      String password = paramMap.get(PASSWORD);
      if (CharSequenceUtil.isNotBlank(password)) {
        // todo 前端不加密不需要
//				try {
//					password = decrypt(password, configProperties.getEncodeKey());
//				}
//				catch (Exception e) {
//					log.error("密码解密失败:{}", password);
//					return Mono.error(e);
//				}
        paramMap.put(PASSWORD, password.trim());
      }

      // jwk:重新构建登陆请求
      URI newUri = UriComponentsBuilder.fromUri(uri).replaceQuery(HttpUtil.toParams(paramMap))
          .build(true)
          .toUri();

      ServerHttpRequest newRequest = exchange.getRequest().mutate().uri(newUri).build();
      return chain.filter(exchange.mutate().request(newRequest).build());
    };
  }

  private static String decrypt(String data, String pass) {
    AES aes = new AES(Mode.CBC, Padding.NoPadding,
        new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM),
        new IvParameterSpec(pass.getBytes()));
    byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
    return new String(result, StandardCharsets.UTF_8);
  }

}
