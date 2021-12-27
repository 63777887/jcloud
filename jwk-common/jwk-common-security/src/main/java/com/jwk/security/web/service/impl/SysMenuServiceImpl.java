package com.jwk.security.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwk.security.web.dao.SysMenuMapper;
import com.jwk.security.web.entity.SysMenu;
import com.jwk.security.web.service.SysMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * `sys_menu` 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements
    SysMenuService {

}