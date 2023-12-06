package com.jwk.upms.web.service;

import com.jwk.upms.base.dto.RegisterReq;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysApi;
import com.jwk.upms.dto.UserImportDto;

import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 认证信息
 * @date 2022/11/22
 */
public interface AuthService {

	UserInfo findUserByName(String name);

	/**
	 * 根据手机找用户
	 * @param phone
	 * @return
	 */
	UserInfo findUserByPhone(String phone);

	/**
	 * 获取资源列表
	 * @return
	 */
	List<SysApi> resourceList();

	Integer testSeata();

	UserInfo findUserByEmail(String mail);

	Boolean register(RegisterReq registerReq);

    Boolean registerImportUsers(List<UserImportDto> datalist);
}
