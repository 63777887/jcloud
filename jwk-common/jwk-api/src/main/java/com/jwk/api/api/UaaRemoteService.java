package com.jwk.api.api;

import com.jwk.api.constant.ServerNameConstants;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.core.model.InnerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Uaa接口
 */
@FeignClient(name = ServerNameConstants.SERVER_UAA, contextId = "testService")
public interface UaaRemoteService {

	/**
	 * 校验token
	 * @param token
	 * @return
	 */
	@GetMapping("/inner/checkToken")
	InnerResponse<UserInfo> checkToken(@RequestParam("token") String token);

}
