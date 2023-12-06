package com.jwk.upms.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.jwk.common.log.enums.LogStatusE;
import com.jwk.common.log.enums.LogTypeE;
import com.jwk.upms.base.entity.SysLog;
import com.jwk.upms.dto.GetSysLogDto;
import com.jwk.upms.web.dao.SysLogMapper;
import com.jwk.upms.web.service.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2023-12-05
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public List<SysLog> getLog(GetSysLogDto getSysLogDto) {
        LambdaQueryChainWrapper<SysLog> lambdaQuery = lambdaQuery();
        if (CollUtil.isNotEmpty(getSysLogDto.getLogTypeList())) {
            lambdaQuery.in(SysLog::getLogType, getSysLogDto.getLogTypeList());
        }
        if (StrUtil.isNotBlank(getSysLogDto.getLogTitle())) {
            lambdaQuery.like(SysLog::getLogTitle, getSysLogDto.getLogTitle());
        }
        if (StrUtil.isNotBlank(getSysLogDto.getRemoteAddr())) {
            lambdaQuery.eq(SysLog::getRemoteAddr, getSysLogDto.getRemoteAddr());
        }
        if (StrUtil.isNotBlank(getSysLogDto.getRequestUri())) {
            lambdaQuery.like(SysLog::getRequestUri, getSysLogDto.getRequestUri());
        }
        if (StrUtil.isNotBlank(getSysLogDto.getMethod())) {
            lambdaQuery.eq(SysLog::getMethod, getSysLogDto.getMethod());
        }
        if (StrUtil.isNotBlank(getSysLogDto.getParams())) {
            lambdaQuery.like(SysLog::getParams, getSysLogDto.getParams());
        }
        if (null != getSysLogDto.getMixTime() && getSysLogDto.getMixTime() > 0) {
            lambdaQuery.ge(SysLog::getTime, getSysLogDto.getMixTime());
        }
        if (null != getSysLogDto.getMaxTime() && getSysLogDto.getMaxTime() > 0) {
            lambdaQuery.le(SysLog::getTime, getSysLogDto.getMaxTime());
        }
        if (StrUtil.isNotBlank(getSysLogDto.getException())) {
            lambdaQuery.like(SysLog::getException, getSysLogDto.getException());
        }
        if (null != getSysLogDto.getStatus() && getSysLogDto.getStatus() > 0) {
            lambdaQuery.eq(SysLog::getStatus, getSysLogDto.getStatus());
        }
        lambdaQuery.orderByDesc(SysLog::getCreateTime);
        return lambdaQuery.list();
    }

    @Override
    public List<SysLog> getLoginLog() {
        return lambdaQuery()
                .in(SysLog::getLogType, Arrays.asList(LogTypeE.USER_LOGIN.getCode(),LogTypeE.USER_LOGOUT.getCode()))
                .eq(SysLog::getStatus, LogStatusE.SUCCESS_LOG.getCode()).orderByDesc(SysLog::getCreateTime).last("limit 5").list();
    }
}
