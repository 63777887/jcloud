package com.jwk.upms.web.controller;

import com.jwk.common.core.model.R;
import com.jwk.upms.base.dto.SysDictItemDto;
import com.jwk.upms.web.service.SysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 字典项 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2024-02-03
 */
@RestController
@RequestMapping("/sysDictItem")
public class SysDictItemController {

	@Autowired
	SysDictItemService sysDictItemService;

	@PostMapping("/getSysDictItem")
	public R getSysDictItem(SysDictItemDto sysDictItemDto) {
		return R.ok(sysDictItemService.getSysDictItem(sysDictItemDto));
	}

}
