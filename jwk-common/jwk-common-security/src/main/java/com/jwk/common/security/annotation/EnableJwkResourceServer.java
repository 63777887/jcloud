package com.jwk.common.security.annotation;

import com.jwk.common.security.config.JwkFeignClientConfiguration;
import com.jwk.common.security.config.JwkResourceServerAutoConfiguration;
import com.jwk.common.security.config.JwkResourceServerConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.4
 * <p>
 * 资源服务注解
 */
@Documented
@Inherited
@EnableWebSecurity
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ JwkResourceServerAutoConfiguration.class, JwkResourceServerConfiguration.class,
		JwkFeignClientConfiguration.class })
public @interface EnableJwkResourceServer {

}
