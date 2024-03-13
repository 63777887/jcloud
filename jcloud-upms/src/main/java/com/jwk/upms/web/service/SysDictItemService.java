package com.jwk.upms.web.service;

import com.jwk.upms.base.dto.SysDictItemDto;
import com.jwk.upms.base.entity.SysDictItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典项 服务类
 * </p>
 *
 * @author jiwk
 * @since 2024-02-03
 */
public interface SysDictItemService extends IService<SysDictItem> {

    List<SysDictItem> getSysDictItem(SysDictItemDto sysDictItemDto);

}
