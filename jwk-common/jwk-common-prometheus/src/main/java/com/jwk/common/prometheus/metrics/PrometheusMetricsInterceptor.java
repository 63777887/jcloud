package com.jwk.common.prometheus.metrics;

import com.jwk.common.prometheus.utils.JwkMetricsUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


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
		long duration = (Long) request.getAttribute("startTime");
		JwkMetricsUtils.pushPrometheus(service, duration,
				response.getStatus() == HttpServletResponse.SC_OK ? Boolean.TRUE : Boolean.FALSE);
	}

}
