package com.jwk.common.shardingjdbc.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jwk.common.shardingjdbc.entity.User;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jiwk
 * @since 2021-09-28
 */
// @DS(value = "mydb")
@DS(value = "master")
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 空方法做测试
	 */
	void addAll();

}
