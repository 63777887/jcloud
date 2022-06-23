package com.jwk.gateway.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 网关配置文件
 */
@Data
@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {

  /**
   * 网关解密登录前端密码 秘钥 {@link com.jwk.gateway.filter.PasswordDecoderFilter}
   */
  private String encodeKey;

  /**
   * 网关不需要校验验证码的客户端
   */
  private List<String> ignoreClients;

}
