package com.jwk.common.canal;

import lombok.Data;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * canal参数配置
 * @date 2022/6/11
 */
@Data
public class CanalProperties {

	public static final String PREFIX = "zeus.canal";

	/**
	 * 保证唯一性, 用于 binlog offset 防重
	 */
	private String id;

	/**
	 * 集群配置
	 */
	private Cluster cluster;

	/**
	 * 单节点配置
	 */
	private SingleNode singleNode = new SingleNode();

	/**
	 * 实例
	 */
	private String instance = "example";

	/**
	 * 过滤
	 */
	private String filter = ".*\\..*";

	/**
	 * 账号
	 */
	private String username = "canal";

	/**
	 * 密码
	 */
	private String password = "canal";

	/**
	 * 拉取数据的间隔 ms
	 */
	private long intervalMillis = 1_000;

	/**
	 * 拉取数据的数量
	 */
	private int batchSize = 100;

	/**
	 * 打印事件日志
	 */
	private boolean showEventLog;

	/**
	 * 打印 Entry 日志
	 */
	private boolean showEntryLog;

	/**
	 * 打印数据明细日志
	 */
	private boolean showRowChange;

	/**
	 * 格式化数据明细日志
	 */
	private boolean formatRowChangeLog;

	/**
	 * 批次达到一定数量进行并行处理, 且确保顺序消费
	 */
	private int performanceThreshold = 10_000;

	/**
	 * 跳过处理
	 */
	private boolean skip;

	/**
	 * 全局逻辑删除字段
	 */
	private String logicDeleteField = "deleted";

	/**
	 * 激活逻辑删除
	 */
	private boolean enableLogicDelete;

	@Data
	public static class Cluster {

		/**
		 * zookeeper host:port
		 */
		private String nodes = "localhost:2181,localhost:2182,localhost:2183";

	}

	@Data
	public static class SingleNode {

		/**
		 * host
		 */
		private String hostname = "localhost";

		/**
		 * port
		 */
		private int port = 11111;

	}

}
