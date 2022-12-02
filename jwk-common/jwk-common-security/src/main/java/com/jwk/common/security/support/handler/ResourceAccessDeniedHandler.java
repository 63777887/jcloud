package com.jwk.common.security.support.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.core.constant.ResponseConstants;
import com.jwk.common.core.model.RestResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 权限异常拦截器
 * @date 2022/11/24
 */
@RequiredArgsConstructor
public class ResourceAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
      response.setCharacterEncoding(JwkSecurityConstants.UTF8);
      response.setContentType(MediaType.APPLICATION_CBOR_VALUE);
      RestResponse<String> result = new RestResponse<>();
      result.setCode(ResponseConstants.ERROR_CODE);
      response.setStatus(HttpStatus.FORBIDDEN.value());
      if (accessDeniedException != null) {
        result.setMsg("error");
        result.setData(accessDeniedException.getMessage());
      }

      PrintWriter printWriter = response.getWriter();
      printWriter.append(objectMapper.writeValueAsString(result));

    // 页面
//    request.setAttribute("message", accessDeniedException.getMessage() + "⭐⭐⭐自定义信息内容⭐⭐⭐我是访问被拒绝去的地方，不是系统默认自定义跳转的方法⭐⭐⭐");
//    String errorHtml = "<!DOCTYPE html>\n"
//        + "<html lang=\"en\">\n"
//        + "<head>\n"
//        + "  <meta charset=\"UTF-8\">\n"
//        + "  <title>403</title>\n"
//        + "</head>\n"
//        + "<body>\n"
//        + "<div class=\"layui-body\">\n"
//        + "  <!-- 内容主体区域 -->\n"
//        + "  <div style=\"padding: 15px;\">\n"
//        + "    <h1>非常抱歉！您没有访问这个功能的权限！</h1>\n"
//        + "    <h2>"
//        + "⭐⭐⭐"
//        + accessDeniedException.getMessage()
//        + "⭐⭐⭐"
//        + "</h2>\n"
//        + "  </div>\n"
//        + "</div>\n"
//        + "</body>\n"
//        + "</html>";
//    response.setContentType(MediaType.TEXT_HTML);
//    response.getWriter().print(errorHtml);
  }
}
