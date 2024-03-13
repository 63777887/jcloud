package com.jwk.upms.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.jwk.common.core.enums.StatusE;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.common.core.utils.DateHelper;
import com.jwk.upms.base.entity.SysSetting;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.web.dao.SysSettingMapper;
import com.jwk.upms.web.service.SysSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2024-01-21
 */
@Service
public class SysSettingServiceImpl extends ServiceImpl<SysSettingMapper, SysSetting> implements SysSettingService {

    @Override
    public Boolean updateSysSetting(SysSetting sysSetting, SysUser sysUser) {
        if (sysSetting.getId() == null || sysSetting.getId() <= 0) {
            throw new ServiceException("设置ID不能为空");
        }
        LambdaUpdateChainWrapper<SysSetting> lambdaUpdate = lambdaUpdate();
        lambdaUpdate.eq(SysSetting::getOrgId, sysUser.getOrgId());
        lambdaUpdate.set(SysSetting::getUpdateTime, DateHelper.nowDate());
        lambdaUpdate.set(SysSetting::getUpdateBy, sysUser.getId());
        if (StrUtil.isNotBlank(sysSetting.getParamName())) {
            lambdaUpdate.set(SysSetting::getParamName, sysSetting.getParamName());
        }
        if (StrUtil.isNotBlank(sysSetting.getParamValue())) {
            lambdaUpdate.set(SysSetting::getParamValue, sysSetting.getParamValue());
        }
        if (null != sysSetting.getStatus() && sysSetting.getStatus() > 0) {
            lambdaUpdate.set(SysSetting::getStatus, sysSetting.getStatus());
        }
        if (StrUtil.isNotBlank(sysSetting.getParamKey())) {
            lambdaUpdate.eq(SysSetting::getParamKey, sysSetting.getParamKey());
        }
        lambdaUpdate.eq(SysSetting::getId, sysSetting.getId()).update();
        return Boolean.TRUE;
    }

    @Override
    public List<SysSetting> getSysSetting(Long orgId, String paramKey, Byte paramType) {
        LambdaQueryChainWrapper<SysSetting> wrapper = lambdaQuery().eq(SysSetting::getOrgId, orgId).eq(SysSetting::getStatus, StatusE.Normal.getId());
        if (StrUtil.isNotBlank(paramKey)) {
            wrapper.eq(SysSetting::getParamKey, paramKey);
        }
        if (paramType != null) {
            wrapper.eq(SysSetting::getParamType, paramType);
        }
        return wrapper.list();
    }
}
