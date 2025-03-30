/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jwk.common.knife4j.config.annotation;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.jwk.common.knife4j.config.SwaggerAutoConfiguration;
import com.jwk.common.knife4j.config.config.OpenAPIDefinition;
import com.jwk.common.knife4j.config.config.SwaggerConfiguration;
import com.jwk.common.knife4j.config.properties.JwkSwaggerProperties;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 开启 pig spring doc
 *
 * @author icar
 * @date 2022-03-26
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(JwkSwaggerProperties.class)
@Import({ SwaggerConfiguration.class, OpenAPIDefinition.class, BeanValidatorPluginsConfiguration.class })
@EnableSwagger2
@EnableKnife4j
public @interface EnableOpenApi {

	/**
	 * 网关路由前缀
	 * @return String
	 */
	String value() default "";

}
