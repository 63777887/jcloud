package com.jwk.common.security.support.component;

import cn.hutool.core.util.StrUtil;
import com.jwk.common.security.annotation.Inner;
import com.jwk.common.core.constant.JwkSecurityConstants;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;

/**
 * @author Jiwk
 * @date 2022/11/24
 * @version 0.1.4
 * <p>
 * Inner注解免鉴权处理
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class JwkSecurityInnerAspect implements Ordered {

	private final HttpServletRequest request;

	@SneakyThrows
	@Around("@within(inner) || @annotation(inner)")
	public Object around(ProceedingJoinPoint point, Inner inner) {
		// 实际注入的inner实体由表达式后一个注解决定，即是方法上的@Inner注解实体，若方法上无@Inner注解，则获取类上的
		if (inner == null) {
			Class<?> clazz = point.getTarget().getClass();
			inner = AnnotationUtils.findAnnotation(clazz, Inner.class);
		}
		String header = request.getHeader(JwkSecurityConstants.FROM);
		if (inner.needFrom() && !StrUtil.equals(JwkSecurityConstants.FROM_IN, header)) {
			if (log.isWarnEnabled()) {
				log.warn("访问接口 {} 没有权限", point.getSignature().getName());
			}
			throw new AccessDeniedException("Access is denied");
		}
		return point.proceed();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

}
