package com.jwk.shardingjdbc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwk.shardingjdbc.entity.User;
import com.jwk.shardingjdbc.mapper.UserMapper;
import com.jwk.shardingjdbc.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2021-09-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
