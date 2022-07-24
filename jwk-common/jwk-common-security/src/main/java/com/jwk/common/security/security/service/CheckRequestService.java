package com.jwk.common.security.security.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 用户校验
 */
public interface CheckRequestService {

  /**
   * 校验token是否合法
   * @param token
   * @return
   */
  default UsernamePasswordAuthenticationToken checkToken(String token){
   return null;
  }

}
