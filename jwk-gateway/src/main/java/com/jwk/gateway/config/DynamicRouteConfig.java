package com.jwk.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "gateway.dynamicRoute", name = "enabled", havingValue = "true")
public class DynamicRouteConfig {
 
    @Resource
    private ApplicationEventPublisher publisher;
 
    /**
     * Nacos实现方式
     */
    @Configuration
    @ConditionalOnProperty(prefix = "gateway.dynamicRoute", name = "dataType", havingValue = "nacos", matchIfMissing = true)
    public class NacosDynamicRoute {
        @Resource
        private NacosConfigProperties nacosConfigProperties;
 
        @Bean
        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository() {
            return new NacosRouteDefinitionRepository(publisher, nacosConfigProperties);
        }
    }
}