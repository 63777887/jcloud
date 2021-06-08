package com.jwk.down.security.handler;

//import com.jwk.down.common.ResponseResult;
import com.alibaba.fastjson.JSON;
import com.jwk.down.enums.ApiStatusE;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class JwtForbiddenConfigHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      AccessDeniedException e) throws IOException, ServletException {
    httpServletResponse.setHeader("Cache-Control","no-cache");
    httpServletResponse.setCharacterEncoding("utf-8");
    httpServletResponse.setContentType("application/json");
    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    httpServletResponse.getWriter().println(JSON.toJSONString(ApiStatusE.SC_FORBIDDEN.getValue()));
    httpServletResponse.getWriter().flush();
  }
}
