package com.jwk.upms.web.controller;


import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * `sys_api_category` 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/sysApiCategory")
@Api(tags = "系统-登陆", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiSupport(author = "jiwk")
public class SysApiCategoryController {

}
