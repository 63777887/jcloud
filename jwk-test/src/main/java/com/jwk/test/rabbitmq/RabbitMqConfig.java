//package com.jwk.test.rabbitmq;
//
//import com.jwk.rabbitmq.Consumer;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.FanoutExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@Slf4j
//public class RabbitMqConfig {
//
//  @Bean(initMethod = "init")
//  public Consumer mytestConsumer(){
//    Consumer consumer = new Consumer();
//    consumer.setQueue(new Queue("ack_queue"));
//    consumer.setRabbiitMqMessageListener(new TestRabbitMqMessage());
//    log.info("===================== ack_queue 启动 =====================");
//    return consumer;
//  }
//  public static final String DEAD_EXCHANGE = "dead_exchange";
//  public static final String DEAD_QUEUE = "dead_queue";
//  public static final String DEAD_KEY = "dead_key";
//  @Bean(initMethod = "init")
//  public Consumer mytestConsumer1(){
//    Consumer consumer = new Consumer();
//    // 设置发送死信队列的参数
//    Map<String, Object> params = new HashMap<>();
//    // 设置过期时间
//    params.put("x-message-ttl",10000);
//    params.put("x-dead-letter-exchange",DEAD_EXCHANGE);
//    params.put("x-dead-letter-routing-key", DEAD_KEY);
//    consumer.setQueue(new Queue("my_test_queue01",true,false,false,params));
//    consumer.setExchange(new FanoutExchange("my_test_exchange01",true,false));
//    consumer.setBinding(new Binding("my_test_queue01", Binding.DestinationType.QUEUE, "my_test_exchange01","",null ));
//    consumer.setRabbiitMqMessageListener(new TestRabbitMqMessage());
//    log.info("===================== ack_queue 启动 =====================");
//    return consumer;
//  }
//
//}
