package com.jwk.common.rabbitmq;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnsCallback;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 自动配置类
 */

@Slf4j
@EnableConfigurationProperties(RabbitmqProperties.class)
public class RabbitMQAutoConfiguration {

	@Bean
	public MessageConverter myMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Resource
	private RabbitmqProperties rabbitmqProperties;

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setAddresses(rabbitmqProperties.addresses);
		cachingConnectionFactory.setUsername(rabbitmqProperties.username);
		cachingConnectionFactory.setPassword(rabbitmqProperties.password);
		cachingConnectionFactory.setVirtualHost(rabbitmqProperties.virtualHost);
		// 消息的确认机制（confirm）；
		// NONE值是禁用发布确认模式，是默认值
		// CORRELATED值是发布消息成功到交换器后会触发回调方法，如1示例
		// SIMPLE值经测试有两种效果，其一效果和CORRELATED值一样会触发回调方法，其二在发布消息成功后使用rabbitTemplate调用waitForConfirms或waitForConfirmsOrDie方法等待broker节点返回发送结果，根据返回结果来判定下一步的逻辑，要注意的点是waitForConfirmsOrDie方法如果返回false则会关闭channel，则接下来无法发送消息到broker;
		cachingConnectionFactory.setPublisherConfirmType(ConfirmType.SIMPLE);

		// 消息无法路由之后通过该channel将消息return给生产者
		cachingConnectionFactory.setPublisherReturns(true);
		// setCacheMode：设置缓存模式，共有两种，CHANNEL和CONNECTION模式。
		// 1、CONNECTION模式，这个模式下允许创建多个Connection，会缓存一定数量的Connection，每个Connection中同样会缓存一些Channel，
		// 除了可以有多个Connection，其它都跟CHANNEL模式一样。
		// 2、CHANNEL模式(默认)，程序运行期间ConnectionFactory会维护着一个Connection，
		// 所有的操作都会使用这个Connection，但一个Connection中可以有多个Channel，
		// 操作rabbitmq之前都必须先获取到一个Channel，
		// 否则就会阻塞（可以通过setChannelCheckoutTimeout()设置等待时间），
		// 这些Channel会被缓存（缓存的数量可以通过setChannelCacheSize()设置）；

		// 设置CONNECTION模式，可创建多个Connection连接
		cachingConnectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);

		// 设置每个Connection中缓存Channel的数量，不是最大的。操作rabbitmq之前（send/receive message等）
		// 要先获取到一个Channel.获取Channel时会先从缓存中找闲置的Channel，如果没有则创建新的Channel，
		// 当Channel数量大于缓存数量时，多出来没法放进缓存的会被关闭。
		cachingConnectionFactory.setChannelCacheSize(100);
		// 单位：毫秒；配合channelCacheSize不仅是缓存数量，而且是最大的数量。
		// 从缓存获取不到可用的Channel时，不会创建新的Channel，会等待这个值设置的毫秒数
		// 同时，在CONNECTION模式，这个值也会影响获取Connection的等待时间，
		// 超时获取不到Connection也会抛出AmqpTimeoutException异常。
		cachingConnectionFactory.setChannelCheckoutTimeout(600);

		// 仅在CONNECTION模式使用，设置Connection的缓存数量。
		cachingConnectionFactory.setConnectionCacheSize(3);
		// setConnectionLimit：仅在CONNECTION模式使用，设置Connection的数量上限。
		cachingConnectionFactory.setConnectionLimit(10);

		return cachingConnectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		// mandatory：交换器无法根据自身类型和路由键找到一个符合条件的队列时的处理方式
		// true：RabbitMQ会调用Basic.Return命令将消息返回给生产者
		// false：RabbitMQ会把消息直接丢弃
		rabbitTemplate.setMandatory(true);

		// ConfirmCallback只确认消息是否到达exchange
		// 以实现方法confirm中ack属性为标准，true到达
		// config : 需要开启rabbitmq得ack publisher-confirm-type
		rabbitTemplate.setConfirmCallback(new ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (ack) {
					if (log.isDebugEnabled()) {
						log.debug("发送消息到exchange成功");
					}
				}
				else {
					if (log.isErrorEnabled()) {
						log.error("发送消息到exchange失败" + cause);
					}
				}
			}
		});

		// ReturnCallback消息没有正确到达队列时触发回调，如果正确到达队列不执行
		// config : 需要开启rabbitmq发送失败回退 rabbitTemplate.setMandatory(true);设置为true
		rabbitTemplate.setReturnsCallback(new ReturnsCallback() {
			@Override
			public void returnedMessage(ReturnedMessage returned) {
				if (log.isErrorEnabled()) {
					log.error("匹配queue失败:{}", returned.toString());
				}
			}
		});

		return rabbitTemplate;
	}

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

		// autoStartup 必须要设为 true ，否则Spring容器不会加载RabbitAdmin类
		rabbitAdmin.setAutoStartup(true);
		return rabbitAdmin;
	}

}