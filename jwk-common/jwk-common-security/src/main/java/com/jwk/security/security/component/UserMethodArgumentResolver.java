package com.jwk.security.security.component;

import com.jwk.security.security.annotation.UserParam;
import com.jwk.security.security.util.SecurityUtils;
import com.jwk.security.web.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 获取用户信息
 */
public class UserMethodArgumentResolver implements HandlerMethodArgumentResolver {
  private static final Logger logger = LoggerFactory.getLogger(UserMethodArgumentResolver.class);

  public UserMethodArgumentResolver() {
  }

  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(UserParam.class);
  }

  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    Class<?> paramType = parameter.getParameterType();
    if (SysUser.class.isAssignableFrom(paramType)) {
      SysUser sysUser = SecurityUtils.getUser();
      if (null == sysUser){
        logger.warn("用户认证过滤器 解析注解失败，SecurityContext不存在用户信息");
        throw new RuntimeException("注解未获取到登录用户信息");
      }
      return sysUser;
      }
     else {
      return null;
    }
  }
}
