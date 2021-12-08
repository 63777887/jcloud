package com.jwk.uaa.comonpent;

import com.jwk.security.security.service.TokenService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 *  * 重写 {@link JwtAccessTokenConverter} 自定义token生成策略,使用Security鉴权时才会使用
 */
public class JwkAccessTokenConverter extends JwtAccessTokenConverter {

//  @Autowired
//  TokenService tokenService;
//
//  @Override
//  protected Map<String, Object> decode(String token) {
//    Map<String, Object> parseMap = tokenService.parseMap(token);
//    parseMap.put(AccessTokenConverter.EXP,Long.valueOf(parseMap.get(AccessTokenConverter.EXP).toString()));
//    return parseMap;
//  }
//
//  @Override
//  protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//    Map<String, ?> map;
//    try {
//      map = getAccessTokenConverter()
//          .convertAccessToken(accessToken, authentication);
//    }
//    catch (Exception e) {
//      throw new IllegalStateException("Cannot convert access token to JSON", e);
//    }
//    HashMap<String, Object> paramMap = new HashMap<>();
//    map.forEach(paramMap::put);
//    return tokenService.generateToken(paramMap);
//  }

}
