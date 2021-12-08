package com.jwk.security.security.service;


import java.util.Date;
import java.util.Map;

public interface TokenService {

  String generateToken(String subject);

  String generateToken(String subject,Long expiration);

  String generateToken(String subject, Date expiration);

  String generateToken(Map<String, Object> claims, Date expiration);

  String generateToken(Map<String, Object> claims);

  String parseSubject(String token);

  long getExpireSec(String token);

  boolean isExpire(String token);

  boolean validateToken(String token);

  String refreshToken(String token);

  Map<String, Object> parseMap(String token);
}
