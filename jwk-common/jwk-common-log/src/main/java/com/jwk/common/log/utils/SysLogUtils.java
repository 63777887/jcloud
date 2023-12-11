package com.jwk.common.log.utils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.jwk.common.core.utils.DateUtil;
import com.jwk.common.log.enums.LogStatusE;
import com.jwk.common.log.enums.LogTypeE;
import com.jwk.common.log.event.SysLogEvent;
import com.jwk.upms.base.dto.SysLogDto;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 系统日志工具类
 * @date 2023/12/11
 */
@UtilityClass
public class SysLogUtils {

	/**
	 * 获取日志对象
	 * @return
	 */
	public SysLogDto getSysLog() {
		HttpServletRequest request = ((ServletRequestAttributes) Objects
			.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		SysLogDto sysLog = new SysLogDto();
		sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
		sysLog.setMethod(request.getMethod());
		sysLog.setRemoteAddr(ServletUtil.getClientIP(request));
		sysLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		sysLog.setParams(HttpUtil.toParams(request.getParameterMap()));
		sysLog.setCreateTime(DateUtil.nowDate());
		return sysLog;
	}

	/**
	 * 推送退出登陆日志事件
	 * @param userId
	 * @param clientId
	 */
	public void pushLogoutLog(Long userId, String clientId) {
		SysLogDto sysLogDto = getSysLog();
		sysLogDto.setLogType(LogTypeE.USER_LOGOUT.getCode());
		sysLogDto.setLogTitle(LogTypeE.USER_LOGOUT.getMsg());
		pushSuccessLog(userId, clientId, sysLogDto);
	}

	/**
	 * 推送成功登陆日志事件
	 * @param userId
	 * @param clientId
	 */
	public void pushLoginSuccessLog(Long userId, String clientId) {
		SysLogDto sysLogDto = getSysLog();
		sysLogDto.setLogType(LogTypeE.USER_LOGIN.getCode());
		sysLogDto.setLogTitle(LogTypeE.USER_LOGIN.getMsg());
		pushSuccessLog(userId, clientId, sysLogDto);
	}

	/**
	 * 推送成功日志
	 * @param userId
	 * @param clientId
	 * @param sysLogDto
	 */
	private void pushSuccessLog(Long userId, String clientId, SysLogDto sysLogDto) {
		sysLogDto.setStatus(LogStatusE.SUCCESS_LOG.getCode());
		sysLogDto.setCreateBy(String.valueOf(userId));
		sysLogDto.setServiceId(clientId);
		SpringUtil.publishEvent(new SysLogEvent(sysLogDto));
	}

}
