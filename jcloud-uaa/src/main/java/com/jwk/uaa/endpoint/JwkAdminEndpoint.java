package com.jwk.uaa.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Jiwk
 * @date 2022/11/24
 * @version 0.1.4
 * <p>
 * 管理页
 */
@Slf4j
@RestController
@RequestMapping
public class JwkAdminEndpoint {

	/**
	 * 认证页面
	 * @param modelAndView
	 * @param error 表单登录失败处理回调的错误信息
	 * @return ModelAndView
	 */
	@GetMapping("/")
	public ModelAndView defaultPage(ModelAndView modelAndView, @RequestParam(required = false) String error) {
		modelAndView.setViewName("defaultPage");
		return modelAndView;
	}

}
