package com.jwk.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Data
public class RabbitMqMessageWrapper implements ChannelAwareMessageListener {

    private RabbiitMqMessageListener rabbiitMqMessageListener;
    private boolean requeue = false;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        //解析message属性
        MessageProperties messageProperties = message.getMessageProperties();
        //解析body消息体
        String body = new String(message.getBody(), messageProperties.getContentEncoding());
        log.info("-----------------触发自定义监听器-------------------");
        log.info("messageProperties:" + messageProperties.toString());
        log.info("body:" + body);
        if (rabbiitMqMessageListener.onMessage(message)){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }else {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false, requeue);
        }
    }

}
