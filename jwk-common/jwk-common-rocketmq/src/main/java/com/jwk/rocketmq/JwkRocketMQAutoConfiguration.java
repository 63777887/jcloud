package com.jwk.rocketmq;

import com.jwk.rocketmq.utils.RocketMQPropUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;


/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 自动配置类
 */
@EnableConfigurationProperties(JwkRocketMQProperties.class)
@Import(RocketMQPropUtils.class)
public class JwkRocketMQAutoConfiguration {

}
