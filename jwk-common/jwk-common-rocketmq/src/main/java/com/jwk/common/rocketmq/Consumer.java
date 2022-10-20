package com.jwk.common.rocketmq;

import com.jwk.common.rocketmq.utils.RocketMQPropUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private String topic;

	private String subExpression;

	private String consumerGroup;

	private String namesrvAddr;

	private RocketMqMessageListener rocketMqMessageListener;

	public void init() {
		this.logger.debug("启动RocketMq监听...{}", this);
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
		String property = RocketMQPropUtils.getProperty("platform");
		if (StringUtils.isNotBlank(property)) {
			this.consumerGroup = this.consumerGroup + "_" + property;
			this.subExpression = this.subExpression + "_" + property;
		}

		consumer.setConsumerGroup(this.consumerGroup);
		consumer.setNamesrvAddr(this.namesrvAddr);
		consumer.setVipChannelEnabled(false);

		try {
			consumer.subscribe(this.topic, this.subExpression);
			RocketMqMessageWrapper rocketMqMessageWrapper = new RocketMqMessageWrapper();
			if (this.rocketMqMessageListener == null) {
				throw new RuntimeException("please define a rocketMqMessageListener for consumer!");
			}

			rocketMqMessageWrapper.setRocketMqMessageListener(this.rocketMqMessageListener);
			consumer.registerMessageListener(rocketMqMessageWrapper);
			consumer.start();
			this.logger.debug("启动RocketMq监听成功！");
		}
		catch (Exception var4) {
			var4.printStackTrace();
		}

	}

	@Override
	public String toString() {
		return "Consumer{topic='" + this.topic + '\'' + ", subExpression='" + this.subExpression + '\''
				+ ", consumerGroup='" + this.consumerGroup + '\'' + ", namesrvAddr='" + this.namesrvAddr + '\''
				+ ", rocketMqMessageListener=" + this.rocketMqMessageListener + '}';
	}

}
