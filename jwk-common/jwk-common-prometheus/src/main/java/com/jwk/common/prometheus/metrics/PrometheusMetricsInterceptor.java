package com.jwk.common.prometheus.metrics;

import com.jwk.common.prometheus.utils.JwkMetricsUtils;
import io.micrometer.core.instrument.Counter;
import io.prometheus.client.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author wanghuainan
 * @date 2021/9/24
 */
@Component
public class PrometheusMetricsInterceptor implements HandlerInterceptor {

	/**
	 * 业务调用时的前置接口监控
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setAttribute("startTime", System.currentTimeMillis());
		return true;
	}

	/**
	 * 业务调用时的后置接口调用
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @throws Exception
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String service = request.getRequestURI();
		long duration = System.currentTimeMillis() - (Long) request.getAttribute("startTime");
		JwkMetricsUtils.pushPrometheus(service, duration, Boolean.TRUE);
	}

}