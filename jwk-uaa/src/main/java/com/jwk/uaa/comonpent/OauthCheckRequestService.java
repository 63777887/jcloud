package com.jwk.uaa.comonpent;

import com.jwk.security.constants.JwkSecurityConstants;
import com.jwk.security.security.service.CheckRequestService;
import com.jwk.security.security.service.JwkUserDetailsService;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OauthCheckRequestService implements CheckRequestService {

  @Autowired
  private CheckTokenEndpoint checkTokenEndpoint;

  @Autowired
  private JwkUserDetailsService jwkUserDetailsService;

  final String AUTHORITIES = AccessTokenConverter.AUTHORITIES;

  @Override
  public UsernamePasswordAuthenticationToken checkToken(String token) {

      Map<String, ?> map = checkTokenEndpoint.checkToken(token);
//            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);

      // 此处map加载的信息为生成tokenEnhancer时添加的信息
      String username = (String) map.get(JwkSecurityConstants.DETAILS_USERNAME);
      //获取userDetails用户信息
      UserDetails userDetails = jwkUserDetailsService.loadUserByUsername(username);

      if (userDetails != null) {

        // token校验通过，设置身份认证信息
        // 两个参数构造方法表示身份未认证，三个参数构造方法表示身份已认证
        //usernamePasswordAuthenticationToken把getUserDetailsByToken获得的带有认证鉴权信息的userDetails交给security
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(), userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(userDetails);

        return usernamePasswordAuthenticationToken;
      }
      return null;
  }


  private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
    Object authorities = map.get(AUTHORITIES);
    if (authorities instanceof String) {
      return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
    }
    if (authorities instanceof Collection) {
      return AuthorityUtils.commaSeparatedStringToAuthorityList(
          StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
    }
    return AuthorityUtils.NO_AUTHORITIES;
  }
}
