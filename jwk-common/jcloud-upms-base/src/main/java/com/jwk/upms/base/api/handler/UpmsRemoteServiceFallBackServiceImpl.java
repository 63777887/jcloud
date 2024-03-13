package com.jwk.upms.base.api.handler;

import com.jwk.common.core.model.RestResponse;
import com.jwk.upms.base.api.UpmsRemoteService;
import com.jwk.upms.base.dto.RemoveTokenDto;
import com.jwk.upms.base.dto.SysOauthClientDto;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysSetting;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * Umps服务降级
 * @date 2022/6/11
 */
@Component
@Slf4j
public class UpmsRemoteServiceFallBackServiceImpl implements UpmsRemoteService {

	@Setter
	private Throwable cause;

	@Override
	public RestResponse<UserInfo> findUserByName(String name) {
		log.error("feign findUserByName fail:{}", name, cause);
		return null;
	}

	@Override
	public RestResponse<UserInfo> findUserByPhone(String phone) {
		log.error("feign findUserByPhone fail:{}", phone, cause);
		return null;
	}

	@Override
	public RestResponse<UserInfo> findUserByEmail(String email) {
		return null;
	}

	@Override
	public RestResponse<List<SysMenu>> loadUserAuthoritiesByRole(List<String> roleCode) {
		return RestResponse.success(Collections.singletonList(new SysMenu()));
	}

	@Override
	public RestResponse<SysOauthClientDto> getClientDetailsById(String clientId) {
		log.error("feign getClientDetailsById fail:", cause);
		return RestResponse.success(new SysOauthClientDto());
	}

	@Override
	public RestResponse<Integer> testSeata() {
		return null;
	}

	@Override
	public RestResponse<List<SysSetting>> getSysSetting(Long orgIdString, String paramKey, Byte paramType) {
		return null;
	}

	@Override
	public RestResponse<List<SysSetting>> removeToken(RemoveTokenDto removeTokenDto) {
		return null;
	}


}
