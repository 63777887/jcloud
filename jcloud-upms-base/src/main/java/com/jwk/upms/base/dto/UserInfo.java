package com.jwk.upms.base.dto;

import com.jwk.upms.base.entity.SysApi;
import com.jwk.upms.base.entity.SysUser;
import java.util.List;
import lombok.Data;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 用户详情
 */
@Data
public class UserInfo {

	SysUser sysUser;

	List<SysApi> sysApis;

}
