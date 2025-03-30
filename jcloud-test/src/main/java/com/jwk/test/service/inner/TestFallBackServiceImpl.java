package com.jwk.test.service.inner;

import com.jwk.common.core.model.R;
import javax.validation.Valid;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestFallBackServiceImpl implements TestService {

	@Setter
	private Throwable cause;

	@Override
	public R getId(@Valid Long id) {
		log.error("feign 查询用户信息失败:{}", id, cause);
		return R.ok("按客戶自定义,global handlerException----2");
	}

}
