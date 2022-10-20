package com.jwk.common.rocketmq;

import java.util.List;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 消息处理
 */
public interface RocketMqMessageListener {

	/**
	 * 接收处理消息
	 * @param var1
	 * @param var2
	 * @return
	 */
	boolean onMessage(List<MessageExt> var1, ConsumeConcurrentlyContext var2);

}
