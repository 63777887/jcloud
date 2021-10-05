package com.jwk.security.security.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface CheckRequestService {

  default UsernamePasswordAuthenticationToken checkToken(String token){
   return null;
  }

}
