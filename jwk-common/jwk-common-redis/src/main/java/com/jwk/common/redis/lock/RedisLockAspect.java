package com.jwk.common.redis.lock;

import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.constant.StrConstants;
import com.jwk.common.core.spel.ExpressionEvaluator;
import com.jwk.common.core.utils.AssertUtil;
import com.jwk.common.redis.annotation.JwkRedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * redis 限流
 * @date 2022/11/9
 */
@Aspect
@RequiredArgsConstructor
@Order(0)//确保比事务注解先执行，分布式锁在事务外
@Slf4j
public class RedisLockAspect implements ApplicationContextAware {

    private final RedisLockService redisLockService;

    private ApplicationContext applicationContext;

    /**
     * 表达式处理
     */
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();


    /**
     * AOP 环切 注解 @RateLimiter
     */
    @Around("@annotation(lock)")
    public Object aroundRateLimiter(ProceedingJoinPoint point, JwkRedisLock lock) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        // el 表达式
        String lockValue = lock.key();
        String prefixKey = lock.prefixKey();
        int waitTime = lock.waitTime();
        int leaseTime = lock.leaseTime();
        TimeUnit timeUnit = lock.unit();

        AssertUtil.isTrue(StrUtil.isNotBlank(lockValue), "@JwkRedissonLock value must have length; it must not be null or empty");
        String lockKey = evalLimitParam(point, lockValue);
        String prefix = StrUtil.isBlank(prefixKey) ? method.getDeclaringClass().getName() + StrConstants.Jin + method.getName() : prefixKey;//默认方法限定名+注解排名（可能多个）
        lockKey = prefix + StrConstants.COLON + lockKey;

        return redisLockService.executeWithLock(lockKey, waitTime, leaseTime, timeUnit, point::proceed);
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
