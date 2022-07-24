package com.jwk.common.security.security.annotation;

import com.jwk.common.security.security.component.JwkResourceServerAutoConfiguration;
import com.jwk.common.security.security.component.JwkResourceServerTokenRelayAutoConfiguration;
import com.jwk.common.security.security.component.JwkSecurityBeanDefinitionRegistrar;
import com.jwk.common.security.feign.JwkFeignClientConfiguration;
import com.jwk.common.security.security.conf.JwkWebMvcConfig;
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
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
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
