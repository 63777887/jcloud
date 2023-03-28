package com.jwk.common.canal.rocketmq;

import com.alibaba.otter.canal.protocol.FlatMessage;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * @date 2022/6/11
 */
public class CanalMessageConsumer extends AbstractCanalMessageConsumer {

	protected final static Logger logger = LoggerFactory.getLogger(CanalMessageConsumer.class);

	public CanalMessageConsumer() {

	}

	public static void main(String[] args) {
		try {
			final CanalMessageConsumer rocketMqClientExample = new CanalMessageConsumer();
			JwkRocketMQCanalConnector jwkRocketMqCanalConnector = new JwkRocketMQCanalConnector(nameServers, topic,
					groupId, 500, true);
			rocketMqClientExample.setConnector(jwkRocketMqCanalConnector);
			rocketMqClientExample.init();
		}
		catch (Throwable e) {
			logger.error("## Something going wrong when starting up the rocketmq consumer:", e);
			System.exit(0);
		}
	}

	@Override
	public void onMessage() {
		try {
			connector.connect();
			connector.subscribe("*");
			while (running) {
				// 获取message
				List<FlatMessage> messages = connector.getFlatListWithoutAck(100L, TimeUnit.MILLISECONDS);
				filter(messages);
				for (FlatMessage message : messages) {
					long batchId = message.getId();
					if (batchId == -1 || message.getData() == null) {
						// try {
						// Thread.sleep(1000);
						// } catch (InterruptedException e) {
						// }
					}
					else {
						// todo 业务逻辑，根据自己的订阅发到不同的topic
						if ("user_copy".equals(message.getTable())) {
							logger.info(message.toString());
						}
					}
				}

				connector.ack(); // 提交确认
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		finally {
			// connector.disconnect();
			// connector.unsubscribe();
		}
	}

	/**
	 * 只消费增、删、改、删表事件，其它事件暂不支持且会被忽略
	 */
	public <T> List<T> filter(List<T> entries) {

		if (CollectionUtils.isEmpty(entries)) {
			return Collections.emptyList();
		}
		if (entries.get(0) instanceof FlatMessage) {
			List<FlatMessage> flatMessages = entries.stream().map(t -> (FlatMessage) t).collect(Collectors.toList());
			return flatMessages.stream().filter(entry -> entry.getType().equals(FlatMessageType.UPDATE.getName()))
					.filter(entry -> entry.getType().equals(FlatMessageType.INSERT.getName()))
					.filter(entry -> entry.getType().equals(FlatMessageType.DELETE.getName())).map(t -> (T) t)
					.collect(Collectors.toList());
		}
		return null;
	}

}
