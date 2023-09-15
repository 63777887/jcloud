package com.jwk.upms.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jwk.upms.base.entity.SysUser;
import java.util.List;

/**
 * <p>
 *
 * `sys_user` Mapper 接口
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

  int updateUser(List<Long> list);

}
