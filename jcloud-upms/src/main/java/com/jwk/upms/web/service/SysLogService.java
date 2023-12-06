package com.jwk.upms.web.service;

import com.jwk.upms.base.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jwk.upms.dto.GetSysLogDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiwk
 * @since 2023-12-05
 */
public interface SysLogService extends IService<SysLog> {

    List<SysLog> getLog(GetSysLogDto getSysLogDto);

    List<SysLog> getLoginLog();
}
