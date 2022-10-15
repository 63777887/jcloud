package com.jwk.api.api.handler;

import com.jwk.api.api.UaaRemoteService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Uaa服务降级
 */
@Component
public class UaaRemoteServiceFallbackFactory implements FallbackFactory<UaaRemoteService> {

	@Override
	public UaaRemoteService create(Throwable throwable) {
		UaaRemoteServiceFallBackServiceImpl uaaRemoteServiceFallBackServiceImpl = new UaaRemoteServiceFallBackServiceImpl();
		uaaRemoteServiceFallBackServiceImpl.setCause(throwable);
		return uaaRemoteServiceFallBackServiceImpl;
	}

}
