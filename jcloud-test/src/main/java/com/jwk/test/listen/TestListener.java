package com.jwk.test.listen;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 测试监听事件
 * @date 2022/9/16
 */
@Component
public class TestListener implements ApplicationListener<TestApplicationEvent> {

	@Override
	public void onApplicationEvent(TestApplicationEvent event) {
		System.out.println("=========================");
		System.out.println("onApplicationEvent start...");
		System.out.println(event);
	}

}
