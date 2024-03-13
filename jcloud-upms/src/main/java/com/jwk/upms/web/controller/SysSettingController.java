package com.jwk.upms.web.controller;


import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.enums.StatusE;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.security.annotation.Inner;
import com.jwk.common.security.annotation.UserParam;
import com.jwk.common.security.util.SecurityUtils;
import com.jwk.upms.base.entity.SysSetting;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.web.service.SysRoleService;
import com.jwk.upms.web.service.SysSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2024-01-21
 */
@RestController
@RequestMapping("/sysSetting")
@RequiredArgsConstructor
public class SysSettingController {


    private final SysSettingService sysSettingService;

    @Inner
    @GetMapping("/getSysSetting")
    public RestResponse getSysSetting(@RequestParam(value = "orgId", required = false) Long orgId, @RequestParam("paramKey") String paramKey, @RequestParam("paramType") Byte paramType) {
        orgId = orgId == null || orgId == 0L ? SecurityUtils.getUser().getOrgId() : orgId;
        return RestResponse.success(sysSettingService.getSysSetting(orgId, paramKey, paramType));
    }

    @PostMapping("/updateSysSetting")
    public RestResponse updateSysSetting(@RequestBody SysSetting sysSetting, @UserParam SysUser sysUser) {
        return RestResponse.success(sysSettingService.updateSysSetting(sysSetting, sysUser));
    }

}
