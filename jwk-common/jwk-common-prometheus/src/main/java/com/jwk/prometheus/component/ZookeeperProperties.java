package com.jwk.prometheus.component;

import lombok.Data;

@Data
public class ZookeeperProperties {

    // 优先级: address > addressEnv
    private String address = System.getenv("ZOOKEEPER_URL");
    // 服务注册namespace
    private String namespace = System.getenv("PROMETHEUS_SERVICE_NAMESPACE");

    private int sessionTimeout =  100*1000;
    private int connectionTimeout = 50*1000;
    private int retryIntervalMs = 3*1000;
    private int retryNumber = 5;

}
