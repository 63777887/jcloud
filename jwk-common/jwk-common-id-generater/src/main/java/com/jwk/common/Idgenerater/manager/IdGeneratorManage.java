package com.jwk.common.Idgenerater.manager;

import com.jwk.common.Idgenerater.exception.IdGeneratorException;
import com.jwk.common.redis.exception.RedisException;
import java.util.List;

/**
 * @author Jiwk
 * @date 2022/11/8
 * @version 0.1.3
 * <p>
 * id生成器
 */
public interface IdGeneratorManage {

	/**
	 * @author Jiwk
	 * @date 2022/11/2
	 * @version 0.1.3
	 * <p>
	 * 生成一个id
	 */
	long generate(int slotId) throws IdGeneratorException, RedisException;

	/**
	 * @author Jiwk
	 * @date 2022/11/2
	 * @version 0.1.3
	 * <p>
	 * 生成size个id
	 */
	List<Long> generate(int slotId, int size) throws IdGeneratorException, RedisException;

	/**
	 * 路由规则
	 * @param type
	 * @return
	 * @throws IdGeneratorException
	 */
	int slotId(long type) throws IdGeneratorException;

}
