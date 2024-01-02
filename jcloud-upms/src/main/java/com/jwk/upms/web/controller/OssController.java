package com.jwk.upms.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.utils.AssertUtil;
import com.jwk.common.core.utils.MinioService;
import com.jwk.common.security.annotation.Inner;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.web.service.SmsService;
import com.jwk.upms.web.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * `sys_api` 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/oss")
@Slf4j
public class OssController {

	@Autowired
	MinioService minioService;

	/**
	 * 文件上传
	 */
	@PostMapping(value = "/upload")
	public RestResponse upload(MultipartFile file) {
		String upload = minioService.upload(file);
		return RestResponse.success(upload);
	}

	/**
	 * 文件下载
	 */
	@GetMapping(value = "/download")
	public void download(HttpServletResponse response,String fileName) {
		minioService.download(fileName,response);
	}

}
