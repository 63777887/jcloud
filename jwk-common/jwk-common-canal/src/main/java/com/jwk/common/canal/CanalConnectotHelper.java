package com.jwk.common.canal;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 *
 */
public interface CanalConnectotHelper {

	/**
	 * 初始化方法
	 */
	void init();

	/**
	 * 连接
	 */
	void connect();

	/**
	 * 断开连接
	 */
	void disconnect();

}
