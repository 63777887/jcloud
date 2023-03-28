package com.jwk.common.rocketmq;

import com.jwk.common.rocketmq.utils.RocketMQPropUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 自动配置类
 * @date 2022/6/11
 */
@EnableConfigurationProperties(JwkRocketMQProperties.class)
@Import(RocketMQPropUtils.class)
public class JwkRocketMQAutoConfiguration {

}
