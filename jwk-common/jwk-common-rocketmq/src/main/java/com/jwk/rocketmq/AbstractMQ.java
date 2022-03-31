package com.jwk.rocketmq;

import java.util.List;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMQ implements RocketMqMessageListener {
  public static final Logger logger = LoggerFactory.getLogger(AbstractMQ.class);

  public abstract boolean handle(List<MessageExt> var1, ConsumeConcurrentlyContext var2);

  @Override
  public boolean onMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
    return this.handle(list, consumeConcurrentlyContext);
  }
}
