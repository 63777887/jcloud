package com.jwk.common.prometheus;

import com.jwk.common.prometheus.config.JwkPrometheusConfiguration;
import com.jwk.common.prometheus.metrics.PrometheusMetricsInterceptor;
import com.jwk.common.prometheus.metrics.WebConfig;
import com.jwk.common.prometheus.properties.JwkPrometheusProperties;
import com.jwk.common.prometheus.support.JwkPrometheusFactory;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 自动配置类
 */
@EnableConfigurationProperties(JwkPrometheusProperties.class)
@Import({ JwkPrometheusConfiguration.class, PrometheusMetricsInterceptor.class, WebConfig.class })
@ConditionalOnProperty(prefix = "jwk.prometheus", name = "enabled", havingValue = "true")
public class JwkPrometheusAutoConfiguration {

	private static Logger logger = LoggerFactory.getLogger(JwkPrometheusAutoConfiguration.class);

	public JwkPrometheusAutoConfiguration(ApplicationContext applicationContext, MeterRegistry registry,
			JwkPrometheusProperties jwkPrometheusProperties) {
		JwkPrometheusFactory.init(applicationContext, registry, jwkPrometheusProperties);
	}

}
