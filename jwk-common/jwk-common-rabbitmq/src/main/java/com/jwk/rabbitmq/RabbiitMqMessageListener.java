package com.jwk.rabbitmq;

import org.springframework.amqp.core.Message;

public interface RabbiitMqMessageListener {

  boolean onMessage(Message message);
}
