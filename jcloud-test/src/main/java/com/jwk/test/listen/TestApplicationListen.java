package com.jwk.test.listen;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 测试
 * @date 2022/9/16
 */
@Component
public class TestApplicationListen implements ApplicationContextAware, BeanFactoryPostProcessor {

	@Autowired
	ApplicationContext applicationContext;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
			throws BeansException {
		applicationContext.publishEvent(new TestApplicationEvent("test..."));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
