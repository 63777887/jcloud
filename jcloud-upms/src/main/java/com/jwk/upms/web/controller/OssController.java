package com.jwk.upms.web.controller;

import com.jwk.common.core.model.R;
import com.jwk.common.core.utils.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public R upload(MultipartFile file) {
		String upload = minioService.upload(file);
		return R.ok(upload);
	}

	/**
	 * 文件下载
	 */
	@GetMapping(value = "/download")
	public void download(HttpServletResponse response, String fileName) {
		minioService.download(fileName, response);
	}

}
