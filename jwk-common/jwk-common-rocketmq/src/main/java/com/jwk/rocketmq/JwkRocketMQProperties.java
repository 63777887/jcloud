package com.jwk.rocketmq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwk.rocketmq")
@Data
public class JwkRocketMQProperties {
  private String namesrvAddr;
  private String producerGroup;
  private String platform;

}
