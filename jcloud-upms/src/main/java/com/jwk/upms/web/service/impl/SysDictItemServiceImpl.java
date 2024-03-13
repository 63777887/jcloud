package com.jwk.upms.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.jwk.common.core.enums.StatusE;
import com.jwk.upms.base.dto.SysDictItemDto;
import com.jwk.upms.base.entity.SysDictItem;
import com.jwk.upms.web.dao.SysDictItemMapper;
import com.jwk.upms.web.service.SysDictItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典项 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2024-02-03
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

    @Override
    public List<SysDictItem> getSysDictItem(SysDictItemDto sysDictItemDto) {
        LambdaQueryChainWrapper<SysDictItem> wrapper = lambdaQuery().eq(SysDictItem::getStatus, StatusE.Normal.getId());
        if (StrUtil.isNotBlank(sysDictItemDto.getDictType())){
            wrapper.eq(SysDictItem::getDictType,sysDictItemDto.getDictType());
        }
        if (CollUtil.isNotEmpty(sysDictItemDto.getDictTypes())){
            wrapper.in(SysDictItem::getDictType,sysDictItemDto.getDictTypes());
        }
        return wrapper.list();
    }
}
