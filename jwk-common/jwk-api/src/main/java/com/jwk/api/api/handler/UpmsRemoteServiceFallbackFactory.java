package com.jwk.api.api.handler;

import com.jwk.api.api.UpmsRemoteService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Umps服务降级
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
