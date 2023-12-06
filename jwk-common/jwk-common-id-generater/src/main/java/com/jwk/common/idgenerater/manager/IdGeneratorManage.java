package com.jwk.common.idgenerater.manager;

import com.jwk.common.idgenerater.exception.IdGeneratorException;

import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * id生成器
 * @date 2022/11/8
 */
public interface IdGeneratorManage {

	/**
	 * @author Jiwk
	 * @date 2022/11/2
	 * @version 0.1.3
	 * <p>
	 * 生成一个id
	 */
	long generate(int slotId) throws Throwable;

	/**
	 * @author Jiwk
	 * @date 2022/11/2
	 * @version 0.1.3
	 * <p>
	 * 生成size个id
	 */
	List<Long> generate(int slotId, int size) throws Throwable;

	/**
	 * 路由规则
	 * @param type
	 * @return
	 * @throws IdGeneratorException
	 */
	int slotId(long type) throws IdGeneratorException;

}
