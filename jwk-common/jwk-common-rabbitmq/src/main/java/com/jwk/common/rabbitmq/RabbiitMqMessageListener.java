package com.jwk.common.rabbitmq;

import org.springframework.amqp.core.Message;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 监听器
 */
public interface RabbiitMqMessageListener {

	/**
	 * 接收处理消息
	 * @param message
	 * @return
	 */
	boolean onMessage(Message message);

}
