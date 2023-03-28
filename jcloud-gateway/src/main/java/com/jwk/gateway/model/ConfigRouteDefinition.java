package com.jwk.gateway.model;

import java.util.List;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 路由配置
 * @date 2022/6/11
 */
public class ConfigRouteDefinition {

	private List<RouteDefinition> routes;

	public List<RouteDefinition> getRoutes() {
		return routes;
	}

	public void setRoutes(List<RouteDefinition> routes) {
		this.routes = routes;
	}

}