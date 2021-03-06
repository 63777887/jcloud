package com.jwk.common.security.security.conf;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.URLUtil;
import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.SysApiDto;
import com.jwk.common.security.security.dto.ResourceConfigAttribute;
import com.jwk.common.security.security.dto.SysApi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;


/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 动态认证资源
 */
public class DynamicMetadataSource implements SecurityMetadataSource {

  @Autowired
  UpmsRemoteService upmsRemoteService;


  /**
   * 返回访问路径所需要的资源列表(字符串)
   *
   * @param object
   * @return
   * @throws IllegalArgumentException
   */
  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

    //获取当前请求的路径，filterInvocation与拦截器重的对应
    FilterInvocation invocation = (FilterInvocation) object;
    String url = invocation.getRequestUrl();
    String path = URLUtil.getPath(url);

    //获取所有的资源
    List<SysApiDto> sysApiDtos = upmsRemoteService.resourceList().getData();
    if (CollUtil.isEmpty(sysApiDtos)){
      return new ArrayList<>();
    }
    List<SysApi> allResource = sysApiDtos.
        stream().map(t-> Convert.convert(SysApi.class,t)).collect(
        Collectors.toList());

    //URL进行匹配（"/user/**" -> "/user/add -> true"）
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    //返回一个ResourceConfigAttribute的资源集合，给DynamicAccessDecisionManager使用
    return allResource.stream()
        .filter(t -> antPathMatcher.match(t.getUrl(), path))
        .map(ResourceConfigAttribute::new)
        .collect(Collectors.toList());
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return null;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return true;
  }
}
