package com.jwk.common.security.dto;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 封装资源，自定义资源类
 */
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
