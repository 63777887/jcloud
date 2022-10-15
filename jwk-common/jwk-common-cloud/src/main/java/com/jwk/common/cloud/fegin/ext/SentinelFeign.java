package com.jwk.common.cloud.fegin.ext;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 支持自动降级注入 重写 {@link com.alibaba.cloud.sentinel.feign.SentinelFeign}
 */
public final class SentinelFeign {

	public static SentinelFeign.Builder builder() {
		return new SentinelFeign.Builder();
	}

	public static final class Builder extends Feign.Builder implements ApplicationContextAware {

		private Contract contract = new Contract.Default();

		private ApplicationContext applicationContext;

		private FeignContext feignContext;

		@Override
		public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
			throw new UnsupportedOperationException();
		}

		@Override
		public SentinelFeign.Builder contract(Contract contract) {
			this.contract = contract;
			return this;
		}

		@Override
		public Feign build() {
			super.invocationHandlerFactory(new InvocationHandlerFactory() {
				@Override
				public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {

					// 查找 FeignClient 上的 降级策略
					FeignClient feignClient = AnnotationUtils.findAnnotation(target.type(), FeignClient.class);
					Class<?> fallback = feignClient.fallback();
					Class<?> fallbackFactory = feignClient.fallbackFactory();

					String beanName = feignClient.contextId();
					if (!StringUtils.hasText(beanName)) {
						beanName = feignClient.name();
					}

					Object fallbackInstance;
					FallbackFactory<?> fallbackFactoryInstance;
					if (void.class != fallback) {
						fallbackInstance = getFromContext(beanName, "fallback", fallback, target.type());
						return new SentinelInvocationHandler(target, dispatch,
								new FallbackFactory.Default(fallbackInstance));
					}

					if (void.class != fallbackFactory) {
						fallbackFactoryInstance = (FallbackFactory<?>) getFromContext(beanName, "fallbackFactory",
								fallbackFactory, FallbackFactory.class);
						return new SentinelInvocationHandler(target, dispatch, fallbackFactoryInstance);
					}
					return new SentinelInvocationHandler(target, dispatch);
				}

				private Object getFromContext(String name, String type, Class<?> fallbackType, Class<?> targetType) {
					Object fallbackInstance = feignContext.getInstance(name, fallbackType);
					if (fallbackInstance == null) {
						throw new IllegalStateException(String.format(
								"No %s instance of type %s found for feign client %s", type, fallbackType, name));
					}

					if (!targetType.isAssignableFrom(fallbackType)) {
						throw new IllegalStateException(String.format(
								"Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
								type, fallbackType, targetType, name));
					}
					return fallbackInstance;
				}
			});

			super.contract(new SentinelContractHolder(contract));
			return super.build();
		}

		private Object getFieldValue(Object instance, String fieldName) {
			Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
			field.setAccessible(true);
			try {
				return field.get(instance);
			}
			catch (IllegalAccessException e) {
				// ignore
			}
			return null;
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
			feignContext = this.applicationContext.getBean(FeignContext.class);
		}

	}

}
