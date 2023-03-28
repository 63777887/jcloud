package com.jwk.common.security.support.feign.api.handler;

import com.jwk.common.security.support.feign.api.UpmsRemoteService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * Umps服务降级
 * @date 2022/6/11
 */
@Component
public class UpmsRemoteServiceFallbackFactory implements FallbackFactory<UpmsRemoteService> {

	@Override
	public UpmsRemoteService create(Throwable throwable) {
		UpmsRemoteServiceFallBackServiceImpl upmsRemoteServiceFallBackServiceImpl = new UpmsRemoteServiceFallBackServiceImpl();
		upmsRemoteServiceFallBackServiceImpl.setCause(throwable);
		return upmsRemoteServiceFallBackServiceImpl;
	}

}
