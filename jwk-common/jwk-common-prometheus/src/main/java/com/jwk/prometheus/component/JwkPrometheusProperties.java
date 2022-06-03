package com.jwk.prometheus.component;

import com.jwk.prometheus.constant.JwkPrometheusConstants;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties(prefix = "jwk.prometheus")
public class JwkPrometheusProperties {

    protected final static Logger logger  = LoggerFactory.getLogger(JwkPrometheusProperties.class);

    // 服务名
    private String application;

    private String registryMode = JwkPrometheusConstants.DEFAULT_REGISTER_MODE;

    @NestedConfigurationProperty
    private ZookeeperProperties zookeeper;


}