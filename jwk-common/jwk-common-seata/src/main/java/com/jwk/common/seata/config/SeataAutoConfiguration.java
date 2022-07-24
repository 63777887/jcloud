package com.jwk.common.seata.config;

import com.jwk.common.core.factory.JwkYamlPropertySourceFactory;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:seata-config.yml", factory = JwkYamlPropertySourceFactory.class)
@EnableAutoDataSourceProxy
@Configuration(proxyBeanMethods = false)
public class SeataAutoConfiguration {

}
