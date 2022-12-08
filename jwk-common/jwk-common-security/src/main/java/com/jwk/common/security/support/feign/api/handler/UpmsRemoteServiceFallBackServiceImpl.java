package com.jwk.common.security.support.feign.api.handler;

import com.jwk.common.core.model.InnerResponse;
import com.jwk.common.security.support.feign.api.UpmsRemoteService;
import com.jwk.upms.base.dto.SysOauthClientDto;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysApi;
import java.util.Collections;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Umps服务降级
 */
@Component
@Slf4j
public class UpmsRemoteServiceFallBackServiceImpl implements UpmsRemoteService {

	@Setter
	private Throwable cause;

	@Override
	public InnerResponse<UserInfo> findUserByName(String name) {
		log.error("feign findUserByName fail:{}", name, cause);
		return null;
	}

	@Override
	public InnerResponse<UserInfo> findUserByPhone(String phone) {
		log.error("feign findUserByPhone fail:{}", phone, cause);
		return null;
	}

	@Override
	public InnerResponse<UserInfo> findUserByEmail(String email) {
		return null;
	}

	@Override
	public InnerResponse<List<SysApi>> loadUserAuthoritiesByRole(List<String> roleCode) {
		return InnerResponse.success(Collections.singletonList(new SysApi()));
	}

	@Override
	public InnerResponse<SysOauthClientDto> getClientDetailsById(String clientId) {
		log.error("feign getClientDetailsById fail:", cause);
		return InnerResponse.success(new SysOauthClientDto());
	}

	@Override
	public InnerResponse<Integer> testSeata() {
		return null;
	}

}
