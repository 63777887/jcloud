package com.jwk.common.rabbitmq;

import com.jwk.common.core.exception.ServiceException;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.util.Assert;
import org.springframework.util.ErrorHandler;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 消费者
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Consumer {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private Queue queue;
  private Exchange exchange;
  private Binding binding;
  private boolean retry = false;
  private RabbiitMqMessageListener rabbiitMqMessageListener;

  @Resource
  private RabbitMQAutoConfiguration rabbitConfig;
  @Resource
  AmqpAdmin amqpAdmin;


  public void init() {
    this.logger.debug("启动RabbitMq监听...{}", this);
    Assert.isTrue(null != queue, "queue cannot be null !");
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(rabbitConfig.connectionFactory());
    //设置启动监听超时时间
    container.setConsumerStartTimeout(3000L);
    container.setExposeListenerChannel(true);
    //设置确认模式 设置成手动模式
    container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    //监听处理类
    RabbitMqMessageWrapper rabbitMqMessageWrapper = new RabbitMqMessageWrapper();
    rabbitMqMessageWrapper.setRequeue(retry);
    rabbitMqMessageWrapper.setRabbiitMqMessageListener(rabbiitMqMessageListener);
    //添加监听器
    container.setMessageListener(rabbitMqMessageWrapper);
    //指定监听的队列
    container.setQueues(queue);
    /* setConcurrentConsumers：设置每个MessageListenerContainer将会创建的Consumer的最小数量，默认是1个。 */
    container.setConcurrentConsumers( 1 );
    container.setMaxConcurrentConsumers(Integer.MAX_VALUE );
    /* setPrefetchCount：设置每次请求发送给每个Consumer的消息数量。 */
    container.setPrefetchCount( 1 );
    /* 是否设置Channel的事务。 */
    container.setChannelTransacted( false );
    /* 设置当rabbitmq收到nack/reject确认信息时的处理方式，设为true，扔回queue头部，设为false，丢弃。 */
//    container.setDefaultRequeueRejected( retry );
    /*
     * setErrorHandler：实现ErrorHandler接口设置进去，所有未catch的异常都会由ErrorHandler处理。
     */
    container.setErrorHandler(new ErrorHandler() {
      @Override
      public void handleError(Throwable t) {
        logger.error("RabbitMq监听出错：{}",t.toString());
        throw new ServiceException(t);
      }
    });
    extendMq();
    container.start();
    this.logger.debug("启动RabbitMq监听成功！");
  }

  private void extendMq() {
    if (null != queue) {
      amqpAdmin.declareQueue(queue);
    }
    if (null != exchange) {
      amqpAdmin.declareExchange(exchange);
    }
    if (null != binding) {
      amqpAdmin.declareBinding(binding);
    }
  }

}
