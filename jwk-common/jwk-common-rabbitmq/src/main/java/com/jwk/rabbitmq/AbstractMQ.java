package com.jwk.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

public abstract class AbstractMQ implements RabbiitMqMessageListener {
  public static final Logger logger = LoggerFactory.getLogger(AbstractMQ.class);

  public abstract boolean handle(Message message);

  @Override
  public boolean onMessage(Message message) {
    return handle(message);
  }
}
