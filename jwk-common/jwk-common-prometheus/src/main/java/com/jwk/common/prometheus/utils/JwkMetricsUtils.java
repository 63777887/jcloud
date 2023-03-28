package com.jwk.common.prometheus.utils;

import com.jwk.common.prometheus.constant.JwkPrometheusConstants;
import com.jwk.common.prometheus.support.JwkPrometheusContext;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 提供自定义监控信息工具类
 * @date 2022/6/11
 */
public class JwkMetricsUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwkMetricsUtils.class);

	public static Counter buildCounter(String name, String desc, String... tags) {
		if (logger.isDebugEnabled()) {
			logger.debug("counter metrics build init...  name:{} ,desc :{} ,tags :{}", name, desc, tags);
		}
		return Counter.builder(name).tags(tags).description(desc)
				.register(JwkPrometheusContext.getInstance().getRegistry());
	}

	public static Timer buildTimer(String name, String desc, String... tags) {
		if (logger.isDebugEnabled()) {
			logger.info("timer metrics build init...  name:{} ,desc :{} ,tags :{}", name, desc, tags);
		}
		return Timer.builder(name).tags(tags).description(desc)
				.register(JwkPrometheusContext.getInstance().getRegistry());
	}

	public static Counter getCounter(String methodName, Boolean isSuccess) {
		Counter counter;
		Map<String, Counter> counterCache = JwkPrometheusContext.counterCache;
		if (null != counterCache.get(methodName)) {
			counter = counterCache.get(methodName);
		}
		else {
			String applicationName = JwkPrometheusContext.getInstance().getJwkPrometheusProperties().getApplication();

			counter = buildCounter(JwkPrometheusConstants.JCLOUD_SERVER_FEIGN_METHOD_COUNT,
					JwkPrometheusConstants.COUNTER_DESC, JwkPrometheusConstants.APPLICATION, applicationName,
					JwkPrometheusConstants.TAG_METHOD, methodName, JwkPrometheusConstants.TAG_STATUS,
					isSuccess.toString());

			counterCache.put(methodName, counter);
		}
		return counter;
	}

	public static Timer getTimer(String methodName, Boolean isSuccess) {
		Timer timer;
		Map<String, Timer> timerCache = JwkPrometheusContext.timerCache;
		if (null != timerCache.get(methodName)) {
			timer = timerCache.get(methodName);
		}
		else {
			String applicationName = JwkPrometheusContext.getInstance().getJwkPrometheusProperties().getApplication();

			timer = buildTimer(JwkPrometheusConstants.JCLOUD_SERVER_FEIGN_METHOD_TIMER,
					JwkPrometheusConstants.TIMER_DESC, JwkPrometheusConstants.APPLICATION, applicationName,
					JwkPrometheusConstants.TAG_METHOD, methodName, JwkPrometheusConstants.TAG_STATUS,
					isSuccess.toString());

			timerCache.put(methodName, timer);
		}
		return timer;
	}

	public static void pushPrometheus(String methodName, long invokeBegin, Boolean isSuccess) {
		long invokeEnd = System.currentTimeMillis();
		getCounter(methodName, isSuccess).increment();
		long invokeTime = invokeEnd - invokeBegin;
		getTimer(methodName, isSuccess).record(invokeTime, TimeUnit.MILLISECONDS);
	}

}
