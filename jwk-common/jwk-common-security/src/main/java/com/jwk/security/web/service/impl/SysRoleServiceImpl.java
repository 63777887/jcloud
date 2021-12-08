package com.jwk.security.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwk.security.web.dao.SysRoleMapper;
import com.jwk.security.web.entity.SysRole;
import com.jwk.security.web.service.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * `sys_role` 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements
    SysRoleService {

}
