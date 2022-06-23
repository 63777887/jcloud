package com.jwk.security.security.conf;

import com.jwk.security.security.component.JwkAuthProperties;
import com.jwk.security.security.service.CheckRequestService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 用户认证过滤器
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {


  private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Autowired
  private JwkAuthProperties jwkAuthProperties;

  private CheckRequestService checkRequestService;

  public void setCheckRequestService(
      CheckRequestService checkRequestService) {
    this.checkRequestService = checkRequestService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    String authKey = jwkAuthProperties.getTokenHeader();
    String authSchema = jwkAuthProperties.getTokenSchema();
    //首先从这里开始，获取token
    String authHead = request.getHeader(authKey);
    if (authHead != null && authHead.contains(authSchema)) {
      if (authHead.startsWith(authSchema)) {
        // 去除SCHEMA信息，得到完整的token
        String token = authHead.substring(authSchema.length());
          // 获取用户名
          try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = checkRequestService
                .checkToken(token);
            SecurityContextHolder.getContext()
                  .setAuthentication(usernamePasswordAuthenticationToken);
          } catch (Exception e) {
            LOGGER.warn("认证异常", e);
          }
      }
    }
    filterChain.doFilter(request, response);
  }
}
