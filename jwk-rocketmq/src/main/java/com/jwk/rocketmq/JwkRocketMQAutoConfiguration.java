package com.jwk.rocketmq;

import com.jwk.rocketmq.utils.RocketMQPropUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RocketMQPropUtils.class)
public class JwkRocketMQAutoConfiguration {

}
