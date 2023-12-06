package com.jwk.upms.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.dto.UserDto;
import com.jwk.upms.web.vo.UserVo;
import java.util.List;

/**
 * <p>
 * `sys_user` 服务类
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
public interface SysUserService extends IService<SysUser> {

	UserInfo findUserById(Long id);

	Page<UserVo> getUserList(Page<SysUser> page, UserDto userDto);

	UserVo getUserById(Long id);

	Boolean updateUser(UserDto userDto);

	Boolean deleteUser(Long id);

	Boolean deleteBatch(List<Long> userIds);

}
