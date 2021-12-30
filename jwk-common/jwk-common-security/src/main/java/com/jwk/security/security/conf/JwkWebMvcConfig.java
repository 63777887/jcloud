package com.jwk.security.security.conf;

import com.jwk.security.security.component.UserMethodArgumentResolver;
import java.util.List;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfigurer配置
 */
public class JwkWebMvcConfig implements WebMvcConfigurer {


  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new UserMethodArgumentResolver());
  }

}
