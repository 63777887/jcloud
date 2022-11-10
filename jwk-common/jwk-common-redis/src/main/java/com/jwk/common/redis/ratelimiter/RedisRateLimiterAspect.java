package com.jwk.common.redis.ratelimiter;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.spel.ExpressionEvaluator;
import com.jwk.common.redis.annotation.JwkRateLimiter;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * @author Jiwk
 * @date 2022/11/9
 * @version 0.1.3
 * <p>
 * redis 限流
 */
@Aspect
@RequiredArgsConstructor
public class RedisRateLimiterAspect implements ApplicationContextAware {
	/**
	 * 表达式处理
	 */
	private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
	/**
	 * redis 限流服务
	 */
	private final RedisRateLimiterClient rateLimiterClient;
	private ApplicationContext applicationContext;

	/**
	 * AOP 环切 注解 @RateLimiter
	 */
	@Around("@annotation(limiter)")
	public Object aroundRateLimiter(ProceedingJoinPoint point, JwkRateLimiter limiter) throws Throwable {
		String limitKey = limiter.value();
		Assert.hasText(limitKey, "@RateLimiter value must have length; it must not be null or empty");
		// el 表达式
		String limitParam = limiter.param();
		// 表达式不为空
		String rateKey;
		if (StrUtil.isNotBlank(limitParam)) {
			String evalAsText = evalLimitParam(point, limitParam);
			rateKey = limitKey + CharPool.COLON + evalAsText;
		} else {
			rateKey = limitKey;
		}
		long max = limiter.max();
		long ttl = limiter.ttl();
		TimeUnit timeUnit = limiter.timeUnit();
		return rateLimiterClient.allow(rateKey, max, ttl, timeUnit, point::proceed);
	}

	/**
	 * 计算参数表达式
	 *
	 * @param point      ProceedingJoinPoint
	 * @param limitParam limitParam
	 * @return 结果
	 */
	private String evalLimitParam(ProceedingJoinPoint point, String limitParam) {
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();
		Object[] args = point.getArgs();
		Object target = point.getTarget();
		Class<?> targetClass = target.getClass();
		EvaluationContext context = evaluator.createContext(method, args, target, targetClass, applicationContext);
		AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
		return evaluator.evalAsText(limitParam, elementKey, context);
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
