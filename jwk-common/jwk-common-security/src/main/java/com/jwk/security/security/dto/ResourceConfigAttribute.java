package com.jwk.security.security.dto;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;


//封装资源，转换资源格式，换成符合权限验证的格式
public class ResourceConfigAttribute implements ConfigAttribute, GrantedAuthority {

  private SysApi sysApi;

  public ResourceConfigAttribute(SysApi sysApi) {
    this.sysApi = sysApi;
  }


  @Override
  public String getAttribute() {
    return sysApi.getUrl();
  }

  public SysApi getSysApi() {
    return sysApi;
  }

  public void setSysApi(SysApi sysApi) {
    this.sysApi = sysApi;
  }

  @Override
  public String getAuthority() {
    return getAttribute();
  }

}
