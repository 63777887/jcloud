package com.jwk.security.security.conf;

import com.jwk.security.constants.SecurityConstant;
import com.jwk.security.security.service.TokenService;
import com.jwk.security.security.service.impl.MyUserDetailsService;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 进行权限认证
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {


  private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


  @Autowired
  private MyUserDetailsService myUserDetailsService;
  @Autowired
  private TokenService tokenService;


  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    //首先从这里开始，获取token
    String cookies = request.getHeader(SecurityConstant.AUTH_KEY);
    if (cookies != null && cookies.contains(SecurityConstant.SCHEMA)) {
      String[] heads = cookies.split(";");
      String authHead = Arrays.stream(heads).filter(t -> t.startsWith(SecurityConstant.SCHEMA))
          .collect(Collectors.toList()).get(0);
      if (authHead.startsWith(SecurityConstant.SCHEMA)) {
      // 去除SCHEMA信息，得到完整的token
      String token = authHead.substring(SecurityConstant.SCHEMA.length());
      if (tokenService.validateToken(token)) {
        // 获取用户名
        String username = tokenService.parseSubject(token);
        try {
          //获取userDetails用户信息
          UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

          if (userDetails != null) {

            // token校验通过，设置身份认证信息
            // 两个参数构造方法表示身份未认证，三个参数构造方法表示身份已认证
            //usernamePasswordAuthenticationToken把getUserDetailsByToken获得的带有认证鉴权信息的userDetails交给security
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    userDetails.getPassword(), userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(userDetails);
            SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
          }
        } catch (Exception e) {
          LOGGER.warn("认证异常", e);
        }
      }
    }
  }
    filterChain.doFilter(request, response);
  }
}
