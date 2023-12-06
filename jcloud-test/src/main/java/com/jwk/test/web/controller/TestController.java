package com.jwk.test.web.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jwk.common.idgenerater.service.IdGeneratorService;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.security.annotation.Inner;
import com.jwk.common.security.annotation.UserParam;
import com.jwk.test.netty.TestServerChannel;
import com.jwk.test.service.TestSeataService;
import com.jwk.test.service.inner.TestService;
import com.jwk.upms.base.entity.SysUser;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web")
public class TestController {

	@Autowired
	private TestService testService;

	@Autowired
	private TestSeataService testSeataService;

	@Autowired
	IdGeneratorService idGeneratorService;

	@GetMapping("/test")
	@SentinelResource(value = "/test")
	public RestResponse advertiseList(Long id) {

		return testService.getId(id);
	}

	@GetMapping("/getId")
	public RestResponse getId(Long id) throws Throwable {
		return RestResponse.success(idGeneratorService.getId(id));
	}

	@GetMapping("/test1")
	public void advertiseList(String msg) {
		TestServerChannel.channelGroup.forEach(channel -> {
			channel.writeAndFlush(msg);
		});
	}
	// @Autowired
	// private ResourceServerTokenServices tokenServices;

	// @Autowired
	// private RemoteTokenServices remoteTokenServices;

	@GetMapping("/test2")
	public SysUser test2(@UserParam SysUser sysUser) {
		return sysUser;
	}

	@Cacheable(value = "get#10000", key = "#key")
	@GetMapping("/get")
	public String get(String key) {
		return key;
	}

	@GetMapping("/test3")
	public int test3() {
		return testSeataService.testSeata();
	}

	@PostMapping("/export1")
	@Inner
	public String export1(HttpServletResponse response, HttpServletRequest request) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + "stat".concat(".xls"));
		workbook.write(response.getOutputStream());
		response.flushBuffer();
		return "导出成功";
	}

	@GetMapping("/testMultiCache")
	@Cacheable(value = "get#6m#1", key = "#key")
	public String testMultiCache(String key) {
		return key;
	}

	@GetMapping("/testRedisCache")
	@Cacheable(value = "get#6m", key = "#key")
	public String testRedisCache(String key) {
		return key;
	}

}
