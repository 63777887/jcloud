package com.jwk.upms.base.api;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.core.constant.ServerNameConstants;
import com.jwk.common.core.model.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * Upms接口
 * @date 2022/6/11
 */
@FeignClient(name = ServerNameConstants.SERVER_UMPS, contextId = "smsRemoteService")
public interface SmsRemoteService {

	/**
	 * 发送短信验证码
	 * @return
	 */
	@PostMapping(value = "/sms/sendCode", headers = JwkSecurityConstants.HEADER_FROM_IN)
	RestResponse sendCode(@RequestParam("phone") String phone);

}
