package com.jwk.security.security.conf;

import com.jwk.security.security.component.JwkAuthProperties;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 权限过滤器
 */
public class DynamicResourceFilter extends AbstractSecurityInterceptor implements Filter {

  /**
   * 查询用户所有资源 查询当前请求路径资源 然后比较路径资源是否在用户资源列表中 UsernamePasswordAuthenticationToken塞入权限信息后，来到这儿做权限校验
   */

  @Autowired
  private DynamicMetadataSource dynamicMetadataSource;

  @Autowired
  private JwkAuthProperties jwkAuthProperties;

  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse,
      FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    AtomicBoolean noAuth = checkAuth(request.getRequestURI());

    // 如果免鉴权，直接放行
    if (noAuth.get()){
      try {
        // 放行
        filterChain.doFilter(servletRequest,servletResponse);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ServletException e) {
        e.printStackTrace();
      }
    }else {

      //创建了一个FilterInvocation对象，这个FilterInvocation对象你可以当作它封装了request，
      // 它的主要工作就是拿请求里面的信息，比如请求的URI
      FilterInvocation filterInvocation = new FilterInvocation(servletRequest, servletResponse,
          filterChain);

      //拿到了一个 Collection<ConfigAttribute>对象，这个对象是一个 List，其实里面就是我们在配置文件中配置的过滤规则。
      //拿到了 Authentication，这里是调用 authenticateIfRequired方法拿到了，其实里面还是通过 SecurityContextHolder拿到的
      //调用了 accessDecisionManager.decide(authenticated, object, attributes)，前两步都是对 decide方法做参数的准备，第三步才是正式去到鉴权的逻辑，既然这里面才是真正鉴权的逻辑，那也就是说鉴权其实是 accessDecisionManager在做。
      InterceptorStatusToken interceptorStatusToken = super.beforeInvocation(filterInvocation);

      try {
        // 放行
        filterInvocation.getChain()
            .doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
      } finally {
        super.afterInvocation(interceptorStatusToken, null);
      }
    }

  }

  private AtomicBoolean checkAuth(String requestUri) {
    String[] noAuthUrlList = jwkAuthProperties.getNoAuthArray();
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    AtomicBoolean noAuth = new AtomicBoolean(false);

    Arrays.stream(noAuthUrlList).forEach(t->{
      if (antPathMatcher.match(t, requestUri)) {
        noAuth.set(true);
      }
    });
    return noAuth;
  }


  @Autowired  //@Autowired 让他在依赖注入时覆盖默认的AccessDecisionManager决策器
  public void setAccessDecisionManager(DynamicAccessDecisionManager decisionManager) {
    //设置AccessDecisionManager
    super.setAccessDecisionManager(decisionManager);
  }

  @Override
  public Class<?> getSecureObjectClass() {
    return FilterInvocation.class;
  }

  @Override
  public SecurityMetadataSource obtainSecurityMetadataSource() {
    //设置SecurityMetadataSource
    return dynamicMetadataSource;
  }

}
