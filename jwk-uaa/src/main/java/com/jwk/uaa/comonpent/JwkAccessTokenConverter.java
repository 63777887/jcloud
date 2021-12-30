//package com.jwk.uaa.comonpent;
//
//import com.jwk.security.security.component.JwkUserAuthenticationConverter;
//import com.jwk.security.security.service.TokenService;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.jwt.JwtHelper;
//import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
//import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//import org.springframework.security.oauth2.common.util.JsonParser;
//import org.springframework.security.oauth2.common.util.JsonParserFactory;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
///**
// *  * 重写 {@link JwtAccessTokenConverter} 自定义token生成策略,使用Security鉴权时才会使用
// */
//public class JwkAccessTokenConverter extends JwtAccessTokenConverter {
//
//  private JsonParser objectMapper = JsonParserFactory.create();
//
////  @Autowired
////  TokenService tokenService;
////
////  @Override
////  protected Map<String, Object> decode(String token) {
////    Map<String, Object> parseMap = tokenService.parseMap(token);
////    parseMap.put(AccessTokenConverter.EXP,Long.valueOf(parseMap.get(AccessTokenConverter.EXP).toString()));
////    return parseMap;
////  }
////
////  @Override
////  protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
////    Map<String, ?> map;
////    try {
////      map = getAccessTokenConverter()
////          .convertAccessToken(accessToken, authentication);
////    }
////    catch (Exception e) {
////      throw new IllegalStateException("Cannot convert access token to JSON", e);
////    }
////    HashMap<String, Object> paramMap = new HashMap<>();
////    map.forEach(paramMap::put);
////    return tokenService.generateToken(paramMap);
////  }
//
//
////  public JwkAccessTokenConverter() {
////    this.setAccessTokenConverter(new JwkUserAuthenticationConverter());
////  }
//
//
//  @Override
//  public void setAccessTokenConverter(AccessTokenConverter tokenConverter) {
//    DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
//    defaultAccessTokenConverter.setUserTokenConverter();
//    super.setAccessTokenConverter(defaultAccessTokenConverter);
//  }
//
//  @Override
//  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
//      OAuth2Authentication authentication) {
//    DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
//    Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
//    String tokenId = result.getValue();
//    if (!info.containsKey(TOKEN_ID)) {
//      info.put(TOKEN_ID, tokenId);
//    }
//    else {
//      tokenId = (String) info.get(TOKEN_ID);
//    }
//    result.setAdditionalInformation(info);
//    result.setValue(encode(result, authentication));
//    OAuth2RefreshToken refreshToken = result.getRefreshToken();
//    if (refreshToken != null) {
//      DefaultOAuth2AccessToken encodedRefreshToken = new DefaultOAuth2AccessToken(accessToken);
//      encodedRefreshToken.setValue(refreshToken.getValue());
//      // Refresh tokens do not expire unless explicitly of the right type
//      encodedRefreshToken.setExpiration(null);
//      try {
//        Map<String, Object> claims = objectMapper
//            .parseMap(JwtHelper.decode(refreshToken.getValue()).getClaims());
//        if (claims.containsKey(TOKEN_ID)) {
//          encodedRefreshToken.setValue(claims.get(TOKEN_ID).toString());
//        }
//      }
//      catch (IllegalArgumentException e) {
//      }
//      Map<String, Object> refreshTokenInfo = new LinkedHashMap<String, Object>(
//          accessToken.getAdditionalInformation());
//      refreshTokenInfo.put(TOKEN_ID, encodedRefreshToken.getValue());
//      refreshTokenInfo.put(ACCESS_TOKEN_ID, tokenId);
//      encodedRefreshToken.setAdditionalInformation(refreshTokenInfo);
//      DefaultOAuth2RefreshToken token = new DefaultOAuth2RefreshToken(
//          encode(encodedRefreshToken, authentication));
//      if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
//        Date expiration = ((ExpiringOAuth2RefreshToken) refreshToken).getExpiration();
//        encodedRefreshToken.setExpiration(expiration);
//        token = new DefaultExpiringOAuth2RefreshToken(encode(encodedRefreshToken, authentication), expiration);
//      }
//      result.setRefreshToken(token);
//    }
//    return result;
//  }
//}
