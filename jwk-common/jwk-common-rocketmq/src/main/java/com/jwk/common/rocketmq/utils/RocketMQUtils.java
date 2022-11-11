package com.jwk.common.rocketmq.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * RocketMQ操作工具类
 */
public class RocketMQUtils {

	private static String namesrvAddr;

	private static String producerGroup;

	private static volatile DefaultMQProducer producer;

	public static Logger logger = LoggerFactory.getLogger(RocketMQUtils.class);

	private static DefaultMQProducer init(String namesrvAddr, String producerGroup) {
		if (null == producer) {
			Class var2 = RocketMQUtils.class;
			synchronized (RocketMQUtils.class) {
				if (null == producer) {
					producer = new DefaultMQProducer(producerGroup);
					producer.setNamesrvAddr(namesrvAddr);

					try {
						producer.start();
					}
					catch (Exception var9) {
						if (logger.isErrorEnabled()) {
							logger.error("初始化producer异常", var9);
						}
					}
					finally {
						;
					}
				}
			}
		}

		return producer;
	}

	public static SendResult send(String topic, String tag, String content, String keys) throws Exception {
		producer = init(RocketMQPropUtils.getProperty("namesrvAddr"), RocketMQPropUtils.getProperty("producerGroup"));
		SendResult sendResult = call(topic, tag, content, keys);
		return sendResult;
	}

	public static SendResult send(String topic, String tag, String content) throws Exception {
		return send(topic, tag, content, (String) null);
	}

	public static SendResult send(String topic, String tag, String content, String namesrvAddress,
			String producerGroupAddress) throws Exception {
		producer = init(namesrvAddress, producerGroupAddress);
		SendResult sendResult = call(topic, tag, content, (String) null);
		return sendResult;
	}

	private static SendResult call(String topic, String tag, String messageBody, String keys) throws Exception {
		String property = RocketMQPropUtils.getProperty("platform");
		SendResult send = null;
		Message msg = new Message();
		msg.setTopic(topic);
		if (StringUtils.isNotBlank(property)) {
			msg.setTags(tag + "_" + property);
		}
		else {
			msg.setTags(tag);
		}

		if (StringUtils.isNotBlank(keys)) {
			msg.setKeys(keys);
		}

		msg.setBody(messageBody.getBytes("UTF-8"));
		send = producer.send(msg);
		return send;
	}

	static {
		System.setProperty("rocketmq.client.logRoot", "/log/rocketmq");
	}

}
