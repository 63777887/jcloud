package com.jwk.prometheus.utils;

import com.jwk.prometheus.config.JwkPrometheusContext;
import com.jwk.prometheus.constant.JwkPrometheusConstants;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwkMetricUtils {


    private static final Logger logger = LoggerFactory.getLogger(JwkMetricUtils.class);


    public static Counter buildCounter(String name, String desc, String ...tags){
        logger.info("counter metrics build init...  name:{} ,desc :{} ,tags :{}",name,desc,tags);
        return Counter.builder(name)
                .tags(tags).description(desc).register(JwkPrometheusContext.getInstance()
                .getRegistry());
    }

    public static Timer buildTimer(String name, String desc, String ...tags){
        logger.info("timer metrics build init...  name:{} ,desc :{} ,tags :{}",name,desc,tags);
        return Timer.builder(name)
            .tags(tags).description(desc).register(JwkPrometheusContext.getInstance()
                .getRegistry());
    }

    public static Counter getCounter(String methodName, Boolean isSuccess){
        Counter counter;
        Map<String, Counter> counterCache = JwkPrometheusContext.counterCache;
        if (null != counterCache.get(methodName)) {
            counter = counterCache.get(methodName);
        } else {
            String applicationName = JwkPrometheusContext.getInstance().getJwkPrometheusProperties()
                .getApplication();

            counter = buildCounter(JwkPrometheusConstants.JCLOUD_SERVER_FEIGN_METHOD_COUNT,
                JwkPrometheusConstants.COUNTER_DESC, JwkPrometheusConstants.APPLICATION,applicationName,
                JwkPrometheusConstants.TAG_METHOD, methodName, JwkPrometheusConstants.TAG_STATUS,isSuccess.toString());

            counterCache.put(methodName, counter);
        }
        return counter;
    }

    public static Timer getTimer(String methodName, Boolean isSuccess){
        Timer timer;
        Map<String, Timer> timerCache = JwkPrometheusContext.timerCache;
        if (null != timerCache.get(methodName)) {
            timer = timerCache.get(methodName);
        } else {
            String applicationName = JwkPrometheusContext.getInstance().getJwkPrometheusProperties()
                .getApplication();

            timer = buildTimer(JwkPrometheusConstants.JCLOUD_SERVER_FEIGN_METHOD_TIMER,
                JwkPrometheusConstants.TIMER_DESC, JwkPrometheusConstants.APPLICATION,applicationName,
                JwkPrometheusConstants.TAG_METHOD, methodName, JwkPrometheusConstants.TAG_STATUS, isSuccess.toString());

            timerCache.put(methodName, timer);
        }
        return timer;
    }
}
