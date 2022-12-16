package com.jwk.common.security.support.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.utils.JwkSpringUtil;
import com.jwk.common.security.annotation.Inner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 配置文件
 */
@ConfigurationProperties(prefix = "jwk.auth")
@Data
@RefreshScope
public class JwkAuthProperties implements InitializingBean {

	/**
	 * 免鉴权路径
	 */
	private String noauthUrl = "";

	public static String innerUrl = "";

	/**
	 * 默认密码
	 */
	private String defaultPassword = "userNotFoundPassword";

	/**
	 * tokenExpireTime
	 */
	private Long expireSec = 86400000L;

	private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

	public String[] getNoAuthArray() {
		return noauthUrl.split(",");
	}

	public String[] getInnerAuthArray() {
		return innerUrl.split(",");
	}

	@Override
	public void afterPropertiesSet() {
		List<String> noAuthUrlList = new ArrayList<>();
		List<String> innerUrlList = new ArrayList<>();
		RequestMappingHandlerMapping mapping = JwkSpringUtil.getBean("requestMappingHandlerMapping");
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		map.keySet().forEach(info -> {
			HandlerMethod handlerMethod = map.get(info);

			// 获取方法上边的注解 替代path variable 为 *
			// /user/get/{id} -> /user/get/*
			Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);

				Optional.ofNullable(method).ifPresent(inner -> {
					info.getPatternsCondition().getPatterns()
								.forEach(url -> noAuthUrlList.add(ReUtil.replaceAll(url, PATTERN, "*")));
					if (method.needFrom()) {
						info.getPatternsCondition().getPatterns()
								.forEach(url -> innerUrlList.add(ReUtil.replaceAll(url, PATTERN, "*")));
					}
				});

			// 获取类上边的注解, 替代path variable 为 *
			Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);

				Optional.ofNullable(controller).ifPresent(inner -> {
					info.getPatternsCondition().getPatterns()
							.forEach(url -> noAuthUrlList.add(ReUtil.replaceAll(url, PATTERN, "*")));
					if (controller.needFrom()) {
						info.getPatternsCondition().getPatterns()
								.forEach(url -> innerUrlList.add(ReUtil.replaceAll(url, PATTERN, "*")));
					}
				});
		});

		// 默认开放接口
		String defaultNoAuthUrl = "/token/*,/swagger-resources/**,/v3/api-docs/**,/v2/api-docs/**"
				+ ",/doc.html";

		if (StrUtil.isNotBlank(noauthUrl)) {
			defaultNoAuthUrl = defaultNoAuthUrl + "," + noauthUrl;
		}
		noauthUrl = defaultNoAuthUrl;
		if (CollUtil.isNotEmpty(noAuthUrlList)) {
			noauthUrl += "," + String.join(",", noAuthUrlList);
		}
		if (CollUtil.isNotEmpty(innerUrlList)){
			innerUrl = String.join(",",innerUrlList);
		}
	}

}
