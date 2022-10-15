package com.jwk.common.rocketmq;

import java.util.List;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 并发消费监听器
 */
public class RocketMqMessageWrapper implements MessageListenerConcurrently {

	private RocketMqMessageListener rocketMqMessageListener;

	public RocketMqMessageWrapper() {
	}

	public RocketMqMessageListener getRocketMqMessageListener() {
		return this.rocketMqMessageListener;
	}

	public void setRocketMqMessageListener(RocketMqMessageListener rocketMqMessageListener) {
		this.rocketMqMessageListener = rocketMqMessageListener;
	}

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
			ConsumeConcurrentlyContext consumeConcurrentlyContext) {
		return this.rocketMqMessageListener.onMessage(list, consumeConcurrentlyContext)
				? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
	}

}
