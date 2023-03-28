package com.jwk.common.core.spel;

import java.lang.reflect.Method;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * ExpressionRootObject
 * @date 2022/11/9
 */
@Getter
@RequiredArgsConstructor
public class ExpressionRootObject {

	private final Method method;

	private final Object[] args;

	private final Object target;

	private final Class<?> targetClass;

	private final Method targetMethod;

}
