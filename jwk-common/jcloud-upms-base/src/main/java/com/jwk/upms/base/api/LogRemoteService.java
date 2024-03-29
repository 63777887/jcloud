package com.jwk.upms.base.api;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.core.constant.ServerNameConstants;
import com.jwk.common.core.model.RestResponse;
import com.jwk.upms.base.dto.SysLogDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * Upms接口
 * @date 2022/6/11
 */
@FeignClient(name = ServerNameConstants.SERVER_UMPS, contextId = "logRemoteService")
public interface LogRemoteService {

	/**
	 * 保存日志
	 * @param sysLogDto 日志参数
	 * @return
	 */
	@PostMapping(value = "/sysLog/saveLog", headers = JwkSecurityConstants.HEADER_FROM_IN)
	RestResponse saveLog(@RequestBody SysLogDto sysLogDto);

}
