package com.jwk.down.security.handler;

import com.alibaba.fastjson.JSON;
import com.jwk.down.enums.ApiStatusE;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 异常处理
 */
public class AuthenticationFailHandler implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException e)
      throws IOException {
    response.setHeader("Cache-Control","no-cache");
    response.setCharacterEncoding("utf-8");
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().println(JSON.toJSONString(ApiStatusE.SC_UNAUTHORIZED.getValue()));
    response.getWriter().flush();
  }
}
