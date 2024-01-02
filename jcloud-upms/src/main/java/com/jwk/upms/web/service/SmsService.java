package com.jwk.upms.web.service;

/**
   * 短信服务
   *
   * @author Jiwk
   * @date 2023/12/13
   * @version 0.1.6
   */
public interface SmsService {

    String sendCode(String phone);

}
