package com.jwk.rocketmq;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(JwkRocketMQProperties.class)
public class JwkRocketMQAutoConfiguration {

}
