package com.jwk.common.canal.rocketmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.google.common.collect.Lists;
import com.alibaba.otter.canal.client.CanalMQConnector;
import com.alibaba.otter.canal.client.CanalMessageDeserializer;
import com.alibaba.otter.canal.client.ConsumerBatchMessage;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.RPCHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 *
 */
public class JwkRocketMQCanalConnector implements CanalMQConnector {

	private static final Logger logger = LoggerFactory.getLogger(JwkRocketMQCanalConnector.class);

	private static final String CLOUD_ACCESS_CHANNEL = "cloud";

	private String nameServer;

	private String topic;

	private String groupName;

	private volatile boolean connected;

	private DefaultMQPushConsumer rocketMQConsumer;

	private BlockingQueue<ConsumerBatchMessage> messageBlockingQueue;

	private int batchSize;

	private long batchProcessTimeout;

	private boolean flatMessage;

	private volatile ConsumerBatchMessage lastGetBatchMessage;

	private String accessKey;

	private String secretKey;

	private String customizedTraceTopic;

	private boolean enableMessageTrace;

	private String accessChannel;

	private String namespace;

	public JwkRocketMQCanalConnector(String nameServer, String topic, String groupName, String accessKey,
			String secretKey, Integer batchSize, boolean flatMessage, boolean enableMessageTrace,
			String customizedTraceTopic, String accessChannel, String namespace) {
		this(nameServer, topic, groupName, accessKey, secretKey, batchSize, flatMessage, enableMessageTrace,
				customizedTraceTopic, accessChannel);
		this.namespace = namespace;
	}

	public JwkRocketMQCanalConnector(String nameServer, String topic, String groupName, String accessKey,
			String secretKey, Integer batchSize, boolean flatMessage, boolean enableMessageTrace,
			String customizedTraceTopic, String accessChannel) {
		this(nameServer, topic, groupName, accessKey, secretKey, batchSize, flatMessage);
		this.enableMessageTrace = enableMessageTrace;
		this.customizedTraceTopic = customizedTraceTopic;
		this.accessChannel = accessChannel;
	}

	public JwkRocketMQCanalConnector(String nameServer, String topic, String groupName, Integer batchSize,
			boolean flatMessage) {
		this.connected = false;
		this.batchSize = -1;
		this.batchProcessTimeout = 60000L;
		this.lastGetBatchMessage = null;
		this.enableMessageTrace = false;
		this.nameServer = nameServer;
		this.topic = topic;
		this.groupName = groupName;
		this.flatMessage = flatMessage;
		this.messageBlockingQueue = new LinkedBlockingQueue(1024);
		this.batchSize = batchSize;
	}

	public JwkRocketMQCanalConnector(String nameServer, String topic, String groupName, String accessKey,
			String secretKey, Integer batchSize, boolean flatMessage) {
		this(nameServer, topic, groupName, batchSize, flatMessage);
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	@Override
	public void connect() throws CanalClientException {
		RPCHook rpcHook = null;
		if (null != this.accessKey && this.accessKey.length() > 0 && null != this.secretKey
				&& this.secretKey.length() > 0) {
			SessionCredentials sessionCredentials = new SessionCredentials();
			sessionCredentials.setAccessKey(this.accessKey);
			sessionCredentials.setSecretKey(this.secretKey);
			rpcHook = new AclClientRPCHook(sessionCredentials);
		}

		this.rocketMQConsumer = new DefaultMQPushConsumer(this.groupName, rpcHook, new AllocateMessageQueueAveragely(),
				this.enableMessageTrace, this.customizedTraceTopic);
		this.rocketMQConsumer.setVipChannelEnabled(false);
		if ("cloud".equals(this.accessChannel)) {
			this.rocketMQConsumer.setAccessChannel(AccessChannel.CLOUD);
		}

		if (!StringUtils.isEmpty(this.namespace)) {
			this.rocketMQConsumer.setNamespace(this.namespace);
		}

		if (!StringUtils.isBlank(this.nameServer)) {
			this.rocketMQConsumer.setNamesrvAddr(this.nameServer);
		}

		if (this.batchSize != -1) {
			this.rocketMQConsumer.setConsumeMessageBatchMaxSize(this.batchSize);
		}

	}

	@Override
	public void disconnect() throws CanalClientException {
		this.rocketMQConsumer.shutdown();
		this.connected = false;
	}

	@Override
	public boolean checkValid() throws CanalClientException {
		return this.connected;
	}

	@Override
	public synchronized void subscribe(String filter) throws CanalClientException {
		if (!this.connected) {
			try {
				if (this.rocketMQConsumer == null) {
					this.connect();
				}

				this.rocketMQConsumer.subscribe(this.topic, filter);
				this.rocketMQConsumer.registerMessageListener(new MessageListenerOrderly() {
					@Override
					public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messageExts,
							ConsumeOrderlyContext context) {
						context.setAutoCommit(true);
						boolean isSuccess = JwkRocketMQCanalConnector.this.process(messageExts);
						return isSuccess ? ConsumeOrderlyStatus.SUCCESS
								: ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
					}
				});
				this.rocketMQConsumer.start();
			}
			catch (MQClientException var3) {
				this.connected = false;
				logger.error("Start RocketMQ consumer error", var3);
			}

			this.connected = true;
		}
	}

	private boolean process(List<MessageExt> messageExts) {
		if (logger.isDebugEnabled()) {
			logger.debug("Get Message: {}", messageExts);
		}

		List messageList = Lists.newArrayList();
		Iterator var3 = messageExts.iterator();

		while (var3.hasNext()) {
			MessageExt messageExt = (MessageExt) var3.next();
			byte[] data = messageExt.getBody();
			if (data != null) {
				try {
					if (!this.flatMessage) {
						Message message = CanalMessageDeserializer.deserializer(data);
						messageList.add(message);
					}
					else {
						FlatMessage flatMessage = (FlatMessage) JSON.parseObject(data, FlatMessage.class,
								new Feature[0]);
						messageList.add(flatMessage);
					}
				}
				catch (Exception var9) {
					logger.error("Add message error", var9);
					throw new CanalClientException(var9);
				}
			}
			else {
				logger.warn("Received message data is null");
			}
		}

		ConsumerBatchMessage batchMessage;
		if (!this.flatMessage) {
			batchMessage = new ConsumerBatchMessage(messageList);
		}
		else {
			batchMessage = new ConsumerBatchMessage(messageList);
		}

		try {
			this.messageBlockingQueue.put(batchMessage);
		}
		catch (InterruptedException var8) {
			logger.error("Put message to queue error", var8);
			throw new RuntimeException(var8);
		}

		boolean isCompleted;
		try {
			isCompleted = batchMessage.waitFinish(this.batchProcessTimeout);
		}
		catch (InterruptedException var7) {
			logger.error("Interrupted when waiting messages to be finished.", var7);
			throw new RuntimeException(var7);
		}

		boolean isSuccess = batchMessage.isSuccess();
		return isCompleted && isSuccess;
	}

	@Override
	public void subscribe() throws CanalClientException {
		this.subscribe((String) null);
	}

	@Override
	public void unsubscribe() throws CanalClientException {
		this.rocketMQConsumer.unsubscribe(this.topic);
	}

	@Override
	public List<Message> getList(Long timeout, TimeUnit unit) throws CanalClientException {
		List<Message> messages = this.getListWithoutAck(timeout, unit);
		if (messages != null && !messages.isEmpty()) {
			this.ack();
		}

		return messages;
	}

	@Override
	public List<Message> getListWithoutAck(Long timeout, TimeUnit unit) throws CanalClientException {
		try {
			if (this.lastGetBatchMessage != null) {
				throw new CanalClientException("mq get/ack not support concurrent & async ack");
			}

			ConsumerBatchMessage batchMessage = (ConsumerBatchMessage) this.messageBlockingQueue.poll(timeout, unit);
			if (batchMessage != null) {
				this.lastGetBatchMessage = batchMessage;
				return batchMessage.getData();
			}
		}
		catch (InterruptedException var4) {
			logger.warn("Get message timeout", var4);
			throw new CanalClientException("Failed to fetch the data after: " + timeout);
		}

		return Lists.newArrayList();
	}

	@Override
	public List<FlatMessage> getFlatList(Long timeout, TimeUnit unit) throws CanalClientException {
		List<FlatMessage> messages = this.getFlatListWithoutAck(timeout, unit);
		if (messages != null && !messages.isEmpty()) {
			this.ack();
		}

		return messages;
	}

	@Override
	public List<FlatMessage> getFlatListWithoutAck(Long timeout, TimeUnit unit) throws CanalClientException {
		try {
			if (this.lastGetBatchMessage != null) {
				throw new CanalClientException("mq get/ack not support concurrent & async ack");
			}

			ConsumerBatchMessage batchMessage = (ConsumerBatchMessage) this.messageBlockingQueue.poll(timeout, unit);
			if (batchMessage != null) {
				this.lastGetBatchMessage = batchMessage;
				return batchMessage.getData();
			}
		}
		catch (InterruptedException var4) {
			logger.warn("Get message timeout", var4);
			throw new CanalClientException("Failed to fetch the data after: " + timeout);
		}

		return Lists.newArrayList();
	}

	@Override
	public void ack() throws CanalClientException {
		try {
			if (this.lastGetBatchMessage != null) {
				this.lastGetBatchMessage.ack();
			}
		}
		catch (Throwable var5) {
			if (this.lastGetBatchMessage != null) {
				this.lastGetBatchMessage.fail();
			}
		}
		finally {
			this.lastGetBatchMessage = null;
		}

	}

	@Override
	public void rollback() throws CanalClientException {
		try {
			if (this.lastGetBatchMessage != null) {
				this.lastGetBatchMessage.fail();
			}
		}
		finally {
			this.lastGetBatchMessage = null;
		}

	}

	@Override
	public Message get(int batchSize) throws CanalClientException {
		throw new CanalClientException("mq not support this method");
	}

	@Override
	public Message get(int batchSize, Long timeout, TimeUnit unit) throws CanalClientException {
		throw new CanalClientException("mq not support this method");
	}

	@Override
	public Message getWithoutAck(int batchSize) throws CanalClientException {
		throw new CanalClientException("mq not support this method");
	}

	@Override
	public Message getWithoutAck(int batchSize, Long timeout, TimeUnit unit) throws CanalClientException {
		throw new CanalClientException("mq not support this method");
	}

	@Override
	public void ack(long batchId) throws CanalClientException {
		throw new CanalClientException("mq not support this method");
	}

	@Override
	public void rollback(long batchId) throws CanalClientException {
		throw new CanalClientException("mq not support this method");
	}

}
