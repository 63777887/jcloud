package com.jwk.common.security.security.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.utils.JwkSpringUtil;
import com.jwk.common.security.security.annotation.Inner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
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
public class JwkAuthProperties implements InitializingBean {

    /**
     * 免鉴权路径
     */
    private String noauthUrl = "";
    /**
     * tokenHeader
     */
    private String tokenHeader="Authorization";
    /**
     * tokenSchema
     */
    private String tokenSchema="Bearer";
    /**
     * tokenSecretKey
     */
    private String secretKey="jiwk";
    /**
     * tokenExpireTime
     */
    private Long expireSec=86400000L;

    private ApplicationContext applicationContext;

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    public String[] getNoAuthArray(){
        String[] noAuthUrlList = noauthUrl.split(",");
        return noAuthUrlList;
    }

    @Override
    public void afterPropertiesSet() {
        List<String> noAuthList = new ArrayList<>();
        RequestMappingHandlerMapping mapping = JwkSpringUtil.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);

            // 获取方法上边的注解 替代path variable 为 *
            // /user/get/{id} -> /user/get/*
            Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
            Optional.ofNullable(method).ifPresent(inner -> info.getPatternsCondition().getPatterns()
                .forEach(url -> noAuthList.add(ReUtil.replaceAll(url, PATTERN, "*"))));

            // 获取类上边的注解, 替代path variable 为 *
            Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);
            Optional.ofNullable(controller).ifPresent(inner -> info.getPatternsCondition().getPatterns()
                .forEach(url -> noAuthList.add(ReUtil.replaceAll(url, PATTERN, "*"))));
        });

        // 默认开放接口
        String defaultNoAuthUrl = "/swagger-resources/**,/v2/api-docs/**,/webjars/**,"
            + "/doc.html,/**/*.css,/**/*.jpg/**/*.jpeg,/**/*.gif,/js/*.js,/**/*.png,/login.jsp,/favicon.ico";

        if (StrUtil.isNotBlank(noauthUrl)){
            defaultNoAuthUrl = defaultNoAuthUrl + "," + noauthUrl;
        }
        noauthUrl = defaultNoAuthUrl;
        if (CollUtil.isNotEmpty(noAuthList)) {
            noauthUrl += "," + String.join(",", noAuthList);
        }
    }
}
