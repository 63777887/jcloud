package com.jwk.uaa.service;

import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.api.UaaRemoteService;
import com.jwk.api.dto.UserInfo;
import com.jwk.api.exception.InternalApiException;
import com.jwk.security.constants.JwkSecurityConstants;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.web.bind.annotation.RestController;

/**
 * see JdbcClientDetailsService
 */

@RestController
public class UaaAuthRemoteServiceImpl implements UaaRemoteService {

	@Autowired
	private CheckTokenEndpoint checkTokenEndpoint;

	@Autowired
	UpmsRemoteService upmsRemoteService;

	@Override
	public UserInfo checkToken(String token) throws InternalApiException {
		Map<String, ?> map = checkTokenEndpoint.checkToken(token);

		// 此处map加载的信息为生成tokenEnhancer时添加的信息
		String username = (String) map.get(JwkSecurityConstants.DETAILS_USERNAME);
		//获取userDetails用户信息

		return upmsRemoteService.findUserByName(username);
	}
}
