package com.jwk.common.prometheus.config;

import com.jwk.common.prometheus.component.JwkPrometheusProperties;
import com.jwk.common.prometheus.constant.JwkPrometheusConstants;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Prometheus工厂类
 */
@Data
public final class JwkPrometheusFactory {

    static Logger logger = LoggerFactory.getLogger(JwkPrometheusFactory.class);

    public static JwkPrometheusContext getContext() {
        JwkPrometheusContext jwkPrometheusContext = JwkPrometheusContext.getInstance();
        return jwkPrometheusContext;
    }

    public static void init(ApplicationContext applicationContext, MeterRegistry registry,
        JwkPrometheusProperties jwkPrometheusProperties) {

        buildJwkPrometheusContext(applicationContext, jwkPrometheusProperties,registry);
    }


    public static void buildJwkPrometheusContext(ApplicationContext applicationContext,
        JwkPrometheusProperties jwkPrometheusProperties,
        MeterRegistry registry) {
        logger.info("jcloud prometheus context build start");
        //默认注册配置为zk
        if (StringUtils.isBlank(jwkPrometheusProperties.getRegistryMode())){
            jwkPrometheusProperties.setRegistryMode(JwkPrometheusConstants.DEFAULT_REGISTER_MODE);
        }
        if (StringUtils.isBlank(jwkPrometheusProperties.getApplication())){
            jwkPrometheusProperties.setApplication(applicationContext.getApplicationName());
        }
        getContext().setApplicationContext(applicationContext)
            .setRegistry(registry)
            .setJwkPrometheusProperties(jwkPrometheusProperties)
            .registry();
    }

}
