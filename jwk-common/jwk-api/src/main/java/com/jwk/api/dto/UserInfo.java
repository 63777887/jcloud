package com.jwk.api.dto;

import java.util.List;
import java.util.regex.Pattern;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 用户详情
 */
@Data
public class UserInfo {

	SysUserDto sysUser;

	List<SysApiDto> sysApis;

}
