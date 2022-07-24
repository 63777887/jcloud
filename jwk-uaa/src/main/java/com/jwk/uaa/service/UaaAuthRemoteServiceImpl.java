package com.jwk.uaa.service;

import com.jwk.api.api.UaaRemoteService;
import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.core.model.InnerResponse;
import com.jwk.common.security.constants.JwkSecurityConstants;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Uaa远程认证
 */
@RestController
public class UaaAuthRemoteServiceImpl implements UaaRemoteService {

	@Autowired
	private CheckTokenEndpoint checkTokenEndpoint;

	@Autowired
	UpmsRemoteService upmsRemoteService;

	@Override
	public InnerResponse<UserInfo> checkToken(String token) {
		Map<String, ?> map = checkTokenEndpoint.checkToken(token);

		// 此处map加载的信息为生成tokenEnhancer时添加的信息
		String username = (String) map.get(JwkSecurityConstants.DETAILS_USERNAME);
		//获取userDetails用户信息

		return InnerResponse.success(upmsRemoteService.findUserByName(username).getData());
	}
}
