package com.jwk.common.knife4j.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import com.jwk.common.knife4j.config.annotation.EnableOpenApi;

import java.util.Map;

public class EnableOpenApiCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableOpenApi.class.getName());
		return attributes != null;
	}

}