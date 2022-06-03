package com.jwk.api.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserInfo {


  SysUserDto sysUser;

  List<SysApiDto> sysApis;

}
