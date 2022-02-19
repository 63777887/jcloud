package com.jwk.test.rabbitmq;

import com.jwk.rabbitmq.AbstractMQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

@Slf4j
public class TestRabbitMqMessage extends AbstractMQ {

  @Override
  public boolean handle(Message message) {
    String body = new String(message.getBody());
    log.info("获取到RabbitMQ消息：{}",message);
    if (body.equals("NO")) {
      Integer.valueOf(body);
      return false;
    }
    return true;
  }
}
