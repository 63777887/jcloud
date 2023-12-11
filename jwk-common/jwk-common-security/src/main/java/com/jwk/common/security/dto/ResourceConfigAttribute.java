package com.jwk.common.security.dto;

import com.jwk.upms.base.entity.SysMenu;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 封装资源，自定义资源类
 * @date 2022/6/11
 */
public class ResourceConfigAttribute implements ConfigAttribute, GrantedAuthority {

	private SysMenu sysMenu;

	public ResourceConfigAttribute(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

	@Override
	public String getAttribute() {
		return sysMenu.getPath();
	}

	public SysMenu getSysMenu() {
		return sysMenu;
	}

	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

	@Override
	public String getAuthority() {
		return getAttribute();
	}

}
