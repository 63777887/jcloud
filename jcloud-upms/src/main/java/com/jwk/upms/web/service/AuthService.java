package com.jwk.upms.web.service;

import com.jwk.api.dto.SysApiDto;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.core.model.InnerResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  List<SysApiDto> resourceList();

  Integer testSeata();

  UserInfo findUserByEmail(String mail);
}
