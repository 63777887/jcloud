package com.jwk.test.service.inner;

import com.jwk.common.core.model.RestResponse;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "jwk-gateway", path = "/jwk-security")
public interface TestService {

	/**
	 * 测试
	 * @param id
	 * @return
	 */
	@GetMapping("/inner/test")
	RestResponse getId(@Valid @RequestParam("id") Long id);

}
