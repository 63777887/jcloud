package com.jwk.test.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

	@Insert("UPDATE `jcloud`.`sys_user` SET `org_id` = 2, `username` = 'jiwk1', `password` = '{bcrypt}$2a$10$fCQ47BQJeqmzs8WhzmAat.VMdHmkDc9IFBzLTvjy9dQ7MPZzm1ITq', `phone` = '17715803071', `email` = '63777887@qq.com', `icon` = NULL, `status` = 1, `create_time` = 20210810140442, `update_time` = NULL WHERE `id` = 4")
	int addOrginId();

}
