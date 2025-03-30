package com.jwk.upms.base.api.handler;

import com.jwk.common.core.model.R;
import com.jwk.upms.base.api.UpmsRemoteService;
import com.jwk.upms.base.dto.RemoveTokenDto;
import com.jwk.upms.base.dto.SysOauthClientDto;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysSetting;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
	public R<UserInfo> findUserByName(String name) {
		log.error("feign findUserByName fail:{}", name, cause);
		return null;
	}

	@Override
	public R<UserInfo> findUserByPhone(String phone) {
		log.error("feign findUserByPhone fail:{}", phone, cause);
		return null;
	}

	@Override
	public R<UserInfo> findUserByEmail(String email) {
		return null;
	}

	@Override
	public R<List<SysMenu>> loadUserAuthoritiesByRole(List<String> roleCode) {
		return R.ok(Collections.singletonList(new SysMenu()));
	}

	@Override
	public R<SysOauthClientDto> getClientDetailsById(String clientId) {
		log.error("feign getClientDetailsById fail:", cause);
		return R.ok(new SysOauthClientDto());
	}

	@Override
	public R<Integer> testSeata() {
		return null;
	}

	@Override
	public R<List<SysSetting>> getSysSetting(Long orgIdString, String paramKey, Byte paramType) {
		return null;
	}

	@Override
	public R<List<SysSetting>> removeToken(RemoveTokenDto removeTokenDto) {
		return null;
	}

}
