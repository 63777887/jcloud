package com.jwk.rocketmq;

import java.util.List;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

public interface RocketMqMessageListener {

  boolean onMessage(List<MessageExt> var1, ConsumeConcurrentlyContext var2);
}
