package com.jwk.common.core.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring(Spring boot)工具封装，包括：
 *
 * <pre>
 *     1、Spring IOC容器中的bean对象获取
 * </pre>
 */
public class JwkSpringUtil extends SpringUtil {

	/**
	 * 动态向Spring注册Bean
	 * <p>
	 * 由{@link org.springframework.beans.factory.BeanFactory} 实现，通过工具开放API
	 *
	 * @param <T>      Bean类型
	 * @param beanName 名称
	 * @param bean     bean
	 * @param initMethod     initMethod
	 */
	public static <T> void registerBean(String beanName, T bean , String initMethod) {
		ListableBeanFactory beanFactory = SpringUtil.getBeanFactory();
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(bean.getClass());
		beanDefinition.setSynthetic(true);
		beanDefinition.setInitMethodName(initMethod);
		if(beanFactory instanceof ConfigurableListableBeanFactory){
			((ConfigurableListableBeanFactory)beanFactory).registerSingleton(beanName, beanDefinition);
		} else if(beanFactory instanceof ConfigurableApplicationContext){
			ConfigurableApplicationContext context = (ConfigurableApplicationContext) beanFactory;
			context.getBeanFactory().registerSingleton(beanName, beanDefinition);
		}
	}
}




