package com.jwk.uaa.constant;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * url地址
 * @date 2022/12/1
 */
public interface JwkOAuth2Urls {

  /**
   * 个性化confirm页面
   */
  String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";

  /**
   * 个性化login页面
   */
  String LOGIN_PAGE = "/token/login";

  /**
   * 登录成功默认跳转页面
   */
  String DEFAULT_SUCCESS_URL = "/";

  /**
   * 登录请求地址
   */
  String LOGIN_PROCESSING_URL = "/token/form";

}
