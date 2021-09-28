package com.jwk.gateway.model;

import java.util.List;
import org.springframework.cloud.gateway.route.RouteDefinition;

public class ConfigRouteDefinition {
    private List<RouteDefinition> routes;
 
    public List<RouteDefinition> getRoutes() {
        return routes;
    }
 
    public void setRoutes(List<RouteDefinition> routes) {
        this.routes = routes;
    }
}