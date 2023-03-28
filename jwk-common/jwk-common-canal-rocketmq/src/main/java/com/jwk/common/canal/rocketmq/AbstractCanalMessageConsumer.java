package com.jwk.common.canal.rocketmq;

import com.alibaba.otter.canal.client.CanalMQConnector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * @date 2022/6/11
 */
public abstract class AbstractCanalMessageConsumer extends AbstractRocektMQTest {

	protected final static Logger logger = LoggerFactory.getLogger(AbstractCanalMessageConsumer.class);

	protected static volatile boolean running = false;

	protected CanalMQConnector connector;

	// ExecutorService executorService = Executors.newSingleThreadExecutor();
	ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "canal-rocketmq-callbackThreadPool-" + r.hashCode());
				}
			});

	private Thread thread = null;

	private Thread.UncaughtExceptionHandler handler = (t, e) -> logger.error("parse events has an error", e);

	public void init() {
		try {
			logger.info("## Start the rocketmq consumer: {}-{}", topic, groupId);
			start();
			logger.info("## The canal rocketmq consumer is running now ......");
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					logger.info("## Stop the rocketmq consumer");
					stop();
				}
				catch (Throwable e) {
					logger.warn("## Something goes wrong when stopping rocketmq consumer:", e);
				}
				finally {
					logger.info("## Rocketmq consumer is down.");
				}
			}));
			while (running) {
				;
			}
		}
		catch (Throwable e) {
			logger.error("## Something going wrong when starting up the rocketmq consumer:", e);
			System.exit(0);
		}
	}

	public void setConnector(CanalMQConnector connector) {
		this.connector = connector;
	}

	private void start() {
		Assert.notNull(connector, "connector is null");
		thread = Executors.defaultThreadFactory().newThread(this::process);
		thread.setUncaughtExceptionHandler(handler);
		executorService.execute(thread);
		running = true;
		// thread.start();
	}

	private void stop() {
		if (!running) {
			return;
		}
		running = false;
		if (null != executorService && !executorService.isShutdown()) {
			try {
				executorService.shutdown();
			}
			catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	private void process() {
		while (!running) {
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
			}
		}

		while (running) {
			onMessage();
		}

		connector.unsubscribe();
		connector.disconnect();
	}

	/**
	 * 接收canal的消息
	 */
	public abstract void onMessage();

}
