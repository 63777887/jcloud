package com.jwk.common.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 接收消息抽象类
 * @date 2022/6/11
 */
public abstract class AbstractMQ implements RabbiitMqMessageListener {

	public static final Logger logger = LoggerFactory.getLogger(AbstractMQ.class);

	/**
	 * 消息处理
	 * @param message
	 * @return
	 */
	public abstract boolean handle(Message message);

	@Override
	public boolean onMessage(Message message) {
		return handle(message);
	}

}
