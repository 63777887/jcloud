//package com.jwk.uaa.service;
//
//import com.jwk.security.security.service.impl.JwkUserDetailsService;
//import java.util.Collection;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//@Component
//public class JwkUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
//
////extractAuthentication
//  private Collection<? extends GrantedAuthority> defaultAuthorities;
//
//  @Autowired
//  private JwkUserDetailsService userDetailsService;
//
//  @Override
//  public Authentication extractAuthentication(Map<String, ?> map) {
//    if (map.containsKey(USERNAME)) {
//      Object principal = map.get(USERNAME);
//      Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
//      if (userDetailsService != null) {
//        UserDetails user = userDetailsService.loadUserByUsername((String) map.get(USERNAME));
//        authorities = user.getAuthorities();
//        principal = user;
//        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
//      }
//      return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
//    }
//    return null;
//  }
//
//  private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
//    if (!map.containsKey(AUTHORITIES)) {
//      return defaultAuthorities;
//    }
//    Object authorities = map.get(AUTHORITIES);
//    if (authorities instanceof String) {
//      return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
//    }
//    if (authorities instanceof Collection) {
//      return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
//          .collectionToCommaDelimitedString((Collection<?>) authorities));
//    }
//    throw new IllegalArgumentException("Authorities must be either a String or a Collection");
//  }
//}
