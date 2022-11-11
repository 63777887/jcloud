package com.jwk.common.prometheus.support;

import com.jwk.common.core.utils.JwkSpringUtil;
import com.jwk.common.prometheus.exception.PrometheusException;
import com.jwk.common.prometheus.exception.PrometheusExceptionCodeE;
import com.jwk.common.prometheus.properties.JwkPrometheusProperties;
import com.jwk.common.prometheus.service.RegistryService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Prometheus上下文
 */
@Data
@Accessors(chain = true)
public class JwkPrometheusContext {

	private static Logger logger = LoggerFactory.getLogger(JwkPrometheusContext.class);

	private static volatile JwkPrometheusContext instance;

	private ApplicationContext applicationContext;

	private JwkPrometheusProperties jwkPrometheusProperties;

	private MeterRegistry registry;

	public static Map<String, Counter> counterCache = new ConcurrentHashMap<>();

	public static Map<String, Timer> timerCache = new ConcurrentHashMap<>();

	private JwkPrometheusContext() {
	}

	public static JwkPrometheusContext getInstance() {
		if (instance == null) {
			synchronized (JwkPrometheusContext.class) {
				if (instance == null) {
					instance = new JwkPrometheusContext();
				}
			}
		}
		return instance;
	}

	public void registry() throws PrometheusException {
			synchronized (JwkPrometheusContext.class) {
				Map<String, RegistryService> registryServiceMap = JwkSpringUtil.getBeansOfType(RegistryService.class);
				String registryMode = jwkPrometheusProperties.getRegistryMode();
				if (null != registryMode) {
					Optional<RegistryService> optional = registryServiceMap.values().stream()
							.filter(service -> service.support().equals(registryMode)).findFirst();
					if (!optional.isPresent()) {
						if (logger.isErrorEnabled()) {
							logger.error("RegistryService error , not registryService instance for {}", registryMode);
						}
						throw new PrometheusException(PrometheusExceptionCodeE.NoRegistryServiceInstance);
					}
					RegistryService registryService = optional.get();
					registryService.registry();
				}
			}
	}

}
