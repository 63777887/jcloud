package com.jwk.gateway.filter;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.jwk.gateway.config.GatewayConfigProperties;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 密码解密工具类
 * @date 2022/6/11
 */
@Slf4j
@RequiredArgsConstructor
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory {

	private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

	private static final String PASSWORD = "password";

	private static final String KEY_ALGORITHM = "AES";

	private final GatewayConfigProperties gatewayConfig;

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			// 1. 不是登录请求，直接向下执行
			if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), "/oauth2/token")) {
				return chain.filter(exchange);
			}

			// 2. 刷新token类型，直接向下执行
			String grantType = request.getQueryParams().getFirst("grant_type");
			if (StrUtil.equals("refresh_token", grantType)) {
				return chain.filter(exchange);
			}

			// 3. 前端加密密文解密逻辑
			Class inClass = String.class;
			Class outClass = String.class;
			ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);

			// 4. 解密生成新的报文
			Mono<?> modifiedBody = serverRequest.bodyToMono(inClass).flatMap(decryptAES());

			BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
			HttpHeaders headers = new HttpHeaders();
			headers.putAll(exchange.getRequest().getHeaders());
			headers.remove(HttpHeaders.CONTENT_LENGTH);

			headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
			CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
			return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
				ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
				return chain.filter(exchange.mutate().request(decorator).build());
			}));
		};
	}

	/**
	 * 原文解密
	 * @return
	 */
	private Function decryptAES() {
		return s -> {
			// 构建前端对应解密AES 因子
			AES aes = new AES(Mode.CFB, Padding.NoPadding,
					new SecretKeySpec(gatewayConfig.getEncodeKey().getBytes(), KEY_ALGORITHM),
					new IvParameterSpec(gatewayConfig.getEncodeKey().getBytes()));

			// 获取请求密码并解密
			Map<String, String> inParamsMap = HttpUtil.decodeParamMap((String) s, CharsetUtil.CHARSET_UTF_8);
			// if (inParamsMap.containsKey(PASSWORD)) {
			if (false) {
				String password = aes.decryptStr(inParamsMap.get(PASSWORD));
				// 返回修改后报文字符
				inParamsMap.put(PASSWORD, password);
			}
			else {
				log.error("非法请求数据:{}", s);
			}
			return Mono.just(HttpUtil.toParams(inParamsMap, Charset.defaultCharset(), true));
		};
	}

	/**
	 * 报文转换
	 * @return
	 */
	private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers,
			CachedBodyOutputMessage outputMessage) {
		return new ServerHttpRequestDecorator(exchange.getRequest()) {
			@Override
			public HttpHeaders getHeaders() {
				long contentLength = headers.getContentLength();
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.putAll(super.getHeaders());
				if (contentLength > 0) {
					httpHeaders.setContentLength(contentLength);
				}
				else {
					httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
				}
				return httpHeaders;
			}

			@Override
			public Flux<DataBuffer> getBody() {
				return outputMessage.getBody();
			}
		};
	}

	// private static final String PASSWORD = "password";
	//
	// private static final String KEY_ALGORITHM = "AES";
	//
	// private static final String OAUTH_TOKEN_URL = "/admin/login";
	//
	// private final GatewayConfigProperties configProperties;
	//
	// @Override
	// public GatewayFilter apply(Object config) {
	// return (exchange, chain) -> {
	// ServerHttpRequest request = exchange.getRequest();
	//
	// // 不是登录请求，直接向下执行
	// if (!CharSequenceUtil.containsAnyIgnoreCase(request.getURI().getPath(),
	// OAUTH_TOKEN_URL)) {
	// return chain.filter(exchange);
	// }
	//
	// // jwk:获取到登陆请求的URL和参数
	// URI uri = exchange.getRequest().getURI();
	// String queryParam = uri.getRawQuery();
	// Map<String, String> paramMap = HttpUtil.decodeParamMap(queryParam,
	// CharsetUtil.CHARSET_UTF_8);
	//
	// // jwk:密码解码后重新加入参数列表
	// String password = paramMap.get(PASSWORD);
	// if (CharSequenceUtil.isNotBlank(password)) {
	// // todo 前端不加密不需要
	// // try {
	// // password = decrypt(password, configProperties.getEncodeKey());
	// // }
	// // catch (Exception e) {
	// // log.error("密码解密失败:{}", password);
	// // return Mono.error(e);
	// // }
	// paramMap.put(PASSWORD, password.trim());
	// }
	//
	// // jwk:重新构建登陆请求
	// URI newUri =
	// UriComponentsBuilder.fromUri(uri).replaceQuery(HttpUtil.toParams(paramMap)).build(true)
	// .toUri();
	//
	// ServerHttpRequest newRequest = exchange.getRequest().mutate().uri(newUri).build();
	// return chain.filter(exchange.mutate().request(newRequest).build());
	// };
	// }
	//
	// private static String decrypt(String data, String pass) {
	// AES aes = new AES(Mode.CBC, Padding.NoPadding, new SecretKeySpec(pass.getBytes(),
	// KEY_ALGORITHM),
	// new IvParameterSpec(pass.getBytes()));
	// byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
	// return new String(result, StandardCharsets.UTF_8);
	// }

}
