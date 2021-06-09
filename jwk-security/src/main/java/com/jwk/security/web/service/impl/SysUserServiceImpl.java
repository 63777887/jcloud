package com.jwk.security.web.service.impl;

import com.jwk.security.web.entity.SysUser;
import com.jwk.security.web.dao.SysUserMapper;
import com.jwk.security.web.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * `sys_user` 服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
