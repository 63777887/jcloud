package com.jwk.rocketmq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwk.rocketmq")
@Data
public class JwkRocketMQProperties {
  private String namesrvAddr;
  private String producerGroup;
  private String platform;

}
