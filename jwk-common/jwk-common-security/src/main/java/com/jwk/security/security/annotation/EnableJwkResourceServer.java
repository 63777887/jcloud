package com.jwk.security.security.annotation;

import com.jwk.security.feign.JwkFeignClientConfiguration;
import com.jwk.security.security.component.JwkResourceServerAutoConfiguration;
import com.jwk.security.security.component.JwkResourceServerTokenRelayAutoConfiguration;
import com.jwk.security.security.component.JwkSecurityBeanDefinitionRegistrar;
import com.jwk.security.security.conf.JwkWebMvcConfig;
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
		JwkResourceServerTokenRelayAutoConfiguration.class, JwkFeignClientConfiguration.class,
		JwkWebMvcConfig.class})
public @interface EnableJwkResourceServer {

}
