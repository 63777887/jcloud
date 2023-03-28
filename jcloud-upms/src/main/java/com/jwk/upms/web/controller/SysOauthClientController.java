package com.jwk.upms.web.controller;

import com.jwk.common.core.model.RestResponse;
import com.jwk.common.security.annotation.Inner;
import com.jwk.upms.base.dto.SysOauthClientDto;
import com.jwk.upms.web.service.SysOauthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 客户端控制器
 * @date 2022/11/25
 */
@RestController
@RequestMapping("/oauthClient")
public class SysOauthClientController {

	@Autowired
	private SysOauthClientService sysOauthClientService;

	/**
	 * 根据clientId查找客户端
	 */
	@GetMapping(value = "/getClientDetailsById/{clientId}")
	@Inner
	public RestResponse<SysOauthClientDto> getClientDetailsById(@PathVariable String clientId) {
		return RestResponse.success(sysOauthClientService.getClientDetailsById(clientId));
	}

}
