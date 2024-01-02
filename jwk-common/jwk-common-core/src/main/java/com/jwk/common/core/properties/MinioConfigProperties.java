package com.jwk.common.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 短信配置
 *
 * @author Jiwk
 * @version 0.1.6
 * @date 2023/12/13
 */
@Data
@ConfigurationProperties("jwk.oss")
public class MinioConfigProperties {

    /**
     * oss开关
     */
    private Boolean enabled = false;

    /**
     * oss地址
     */
    private String address;
    /**
     * 授权key
     */
    private String accessKey;
    /**
     * 授权secretKey
     */
    private String secretKey;
    /**
     * 存储桶
     */
    private String bucket;
}
