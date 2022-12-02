package com.jwk.common.Idgenerater.service;

import com.jwk.common.Idgenerater.exception.IdGeneratorException;
import com.jwk.common.redis.exception.RedisException;
import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * @date 2022/11/2
 */
public interface IdGeneratorService {

	/**
	 * 根据type获取一个id
	 * @param type
	 * @return
	 * @throws IdGeneratorException
	 */
	long getId(long type) throws IdGeneratorException, RedisException;

	/**
	 * 根据type获取一批id
	 * @param type
	 * @param size
	 * @return
	 * @throws IdGeneratorException
	 */
	List<Long> listIds(long type, int size) throws IdGeneratorException, RedisException;

}
