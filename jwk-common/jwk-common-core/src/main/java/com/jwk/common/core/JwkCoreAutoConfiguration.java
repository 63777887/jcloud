package com.jwk.common.core;

import com.jwk.common.core.properties.MinioConfigProperties;
import com.jwk.common.core.properties.SmsConfigProperties;
import com.jwk.common.core.utils.MinioService;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 自动配置
 *
 * @author Jiwk
 * @version 0.1.6
 * @date 2023/12/13
 */
@EnableConfigurationProperties({SmsConfigProperties.class, MinioConfigProperties.class})
public class JwkCoreAutoConfiguration {


    @Bean
    @ConditionalOnProperty(prefix = "jwk.oss", name = "enabled", havingValue = "true")
    public MinioClient minioClient(MinioConfigProperties minioConfigProperties) {
        return MinioClient.builder()
                .endpoint(minioConfigProperties.getAddress())
                .credentials(minioConfigProperties.getAccessKey(), minioConfigProperties.getSecretKey())
                .build();
    }

    @Bean
    @ConditionalOnBean(MinioClient.class)
    public MinioService minioService(MinioClient minioClient,MinioConfigProperties minioConfigProperties) {
        return new MinioService(minioClient,minioConfigProperties);
    }

}
