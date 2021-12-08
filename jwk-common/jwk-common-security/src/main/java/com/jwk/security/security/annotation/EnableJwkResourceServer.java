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

package com.jwk.security.security.annotation;

import com.jwk.security.feign.JwkFeignClientConfiguration;
import com.jwk.security.security.component.JwkResourceServerAutoConfiguration;
import com.jwk.security.security.component.JwkResourceServerTokenRelayAutoConfiguration;
//import com.jwk.security.security.component.JwkSecurityBeanDefinitionRegistrar;
import com.jwk.security.security.component.JwkSecurityBeanDefinitionRegistrar;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * 资源服务注解
 */
@Documented
@Inherited
@EnableResourceServer
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ JwkResourceServerAutoConfiguration.class, JwkSecurityBeanDefinitionRegistrar.class,
		JwkResourceServerTokenRelayAutoConfiguration.class, JwkFeignClientConfiguration.class })
public @interface EnableJwkResourceServer {

}
