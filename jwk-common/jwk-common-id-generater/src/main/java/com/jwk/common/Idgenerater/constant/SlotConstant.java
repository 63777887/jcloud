package com.jwk.common.Idgenerater.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jiwk
 * @date 2022/11/2
 * @version 0.1.3
 * <p>
 * 槽位相关常量
 */
@Slf4j
public class SlotConstant {

	/**
	 * 普通槽总数[0,16384)
	 */
	public final static int GENERAL_SLOT_COUNT = 16384;

	/**
	 * 普通槽位的初始化值，产生的第一个id：初始值+1
	 */
	public final static long GENERAL_SLOT_INITIAL_VALUE = 1;

	/**
	 * 允许生成的最大值
	 */
	public final static long GENERAL_SLOT_MAX_VALUE = 9999999;

	/**
	 * 批量获取id时最大size
	 */
	public final static int MAX_SIZE = 1000;

}
