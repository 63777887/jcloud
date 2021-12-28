package com.jwk.security.security.service.impl;


import com.jwk.security.security.component.JwkAuthProperties;
import com.jwk.security.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TokenServiceImpl implements TokenService {


  @Autowired
  private JwkAuthProperties jwkAuthProperties;

  /**
   * 生成token令牌,加入过期时间
   *
   * @param subject 用户名
   * @return token令牌
   */
  @Override
  public String generateToken(String subject) {
    return generateToken(subject,jwkAuthProperties.getExpireSec());
  }

  @Override
  public String generateToken(String subject, Long expiration) {
    // HMAC SHA256
    return generateToken(subject,expiration,SignatureAlgorithm.HS512,jwkAuthProperties.getSecretKey());
  }

  @Override
  public String generateToken(String subject, Date expiration) {
    // HMAC SHA256
    return Jwts.builder()
        .setSubject(subject)
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, jwkAuthProperties.getSecretKey())
        .compact();
  }


  private String generateToken(String subject, Long expiration,SignatureAlgorithm sign,String secretKey) {
    return Jwts.builder()
        .setSubject(subject)
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(sign, secretKey)
        .compact();
  }

  @Override
  public String generateToken(Map<String, Object> claims, Date expiration) {
    // HMAC SHA256
    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, jwkAuthProperties.getSecretKey())
        .compact();
  }

  @Override
  public String generateToken(Map<String, Object> claims) {
    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(new Date(System.currentTimeMillis() + jwkAuthProperties.getExpireSec()))
        .signWith(SignatureAlgorithm.HS512, jwkAuthProperties.getSecretKey())
        .compact();
  }

  /**
   * 从令牌中获取数据声明,过期也强行获取
   *
   * @param token 令牌
   * @return 数据声明
   */
  private Claims getClaimsFromToken(String token) {
    try {
      return Jwts.parser().setSigningKey(jwkAuthProperties.getSecretKey()).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException e) {
      //过期也解析body
      return e.getClaims();
    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 从令牌中获取用户名
   *
   * @param token 令牌
   * @return 用户名
   */
  @Override
  public String parseSubject(String token) {
    return getClaimsFromToken(token).getSubject();
  }

  /**
   * 从令牌中获取令牌剩余时间，小于等于0即是过期
   *
   * @param token 令牌
   * @return 过期剩余时间
   */
  @Override
  public long getExpireSec(String token) {
    return getClaimsFromToken(token).getExpiration().getTime() - System.currentTimeMillis();
  }

  /**
   * 从令牌判断是否过期,返回true为过期
   *
   * @param token 令牌
   * @return 是否过期
   */
  @Override
  public boolean isExpire(String token) {
    return getExpireSec(token) <= 0;
  }

  /**
   * 验证令牌
   *
   * @param token,username token和usename，判断是否是有效令牌
   * @return 是否有效
   */
  @Override
  public boolean validateToken(String token) {
    Claims claims = getClaimsFromToken(token);
    if (claims.getExpiration() == null
        || claims.getExpiration().getTime() <= System.currentTimeMillis()) {
      throw new IllegalArgumentException("token过期");
    }
    return true;
  }

  /**
   * 刷新令牌
   *
   * @param token 原令牌
   * @return 新令牌
   */
  @Override
  public String refreshToken(String token) {
    Claims claims = getClaimsFromToken(token);
    validateToken(token);
    return generateToken(claims);
  }


  @Override
  public Map<String, Object> parseMap(String token) {
    return getClaimsFromToken(token);
  }


}
