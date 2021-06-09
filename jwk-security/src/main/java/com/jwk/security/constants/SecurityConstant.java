package com.jwk.security.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConstant {



  public static String AUTH_KEY;
  @Value("${token.header}")
  public void setAuthKey(String AUTH_KEY) {
    SecurityConstant.AUTH_KEY = AUTH_KEY;
  }

  public static String SCHEMA;
  @Value("${token.schema}")
  public void setSCHEMA(String SCHEMA) {
    SecurityConstant.SCHEMA = SCHEMA;
  }

}
