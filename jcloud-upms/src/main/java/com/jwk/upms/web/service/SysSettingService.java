package com.jwk.upms.web.service;

import com.jwk.upms.base.entity.SysSetting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jwk.upms.base.entity.SysUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiwk
 * @since 2024-01-21
 */
public interface SysSettingService extends IService<SysSetting> {

    Boolean updateSysSetting(SysSetting sysSetting, SysUser sysUser);

    List<SysSetting> getSysSetting(Long orgId, String paramKey, Byte paramType);
}
