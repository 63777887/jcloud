package com.jwk.uaa.endpoint;

import com.jwk.common.core.model.RestResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * `sys_api` 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class SysApiController {

	/**
	 * 接口列表
	 */
	@GetMapping(value = "/set")
	public RestResponse set(HttpServletRequest request) {
		request.getSession().setAttribute("test","test11111");
		return RestResponse.success();
	}
	/**
	 * 接口列表
	 */
	@GetMapping(value = "/get")
	public RestResponse get(HttpServletRequest request) {
		Object test = request.getSession().getAttribute("test");
		log.info("session: ================ {}",test.toString());
		return RestResponse.success();
	}

}
