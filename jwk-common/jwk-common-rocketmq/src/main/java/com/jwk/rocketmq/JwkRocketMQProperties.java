package com.jwk.rocketmq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 配置文件
 */
@ConfigurationProperties(prefix = "jwk.rocketmq")
@Data
public class JwkRocketMQProperties {
  private String namesrvAddr;
  private String producerGroup;
  private String platform;

}
