package com.jwk.upms.web.controller;


import cn.hutool.core.convert.Convert;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.security.annotation.Inner;
import com.jwk.common.security.annotation.UserParam;
import com.jwk.upms.base.dto.SysLogDto;
import com.jwk.upms.base.entity.SysLog;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.dto.GetSysLogDto;
import com.jwk.upms.web.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2023-12-05
 */
@RestController
@RequestMapping("/sysLog")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @PostMapping("/saveLog")
    @Inner
    public RestResponse saveLog(@RequestBody SysLogDto sysLogDto) {
        SysLog sysLog = Convert.convert(SysLog.class, sysLogDto);
        return RestResponse.success(sysLogService.save(sysLog));
    }

    @PostMapping("/getLog")
    public RestResponse getLog(@RequestBody GetSysLogDto getSysLogDto) {
        return RestResponse.success(sysLogService.getLog(getSysLogDto));
    }

    @GetMapping("/getLoginLog")
    public RestResponse getLoginLog(@UserParam SysUser sysUser,String serviceId ) {
        return RestResponse.success(sysLogService.getLoginLog(sysUser.getId(),serviceId));
    }

}
