package com.jwk.common.idgenerater.manager.impl;

import com.jwk.common.idgenerater.exception.IdExceptionCodeE;
import com.jwk.common.idgenerater.exception.IdGeneratorException;
import com.jwk.common.idgenerater.manager.IdGeneratorManage;
import com.jwk.common.idgenerater.properties.IdGeneraterProperties;
import com.jwk.common.redis.annotation.JwkRedisLock;
import com.jwk.common.redis.lock.RedisLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * id生成器
 * @date 2022/11/8
 */
@Slf4j
@RequiredArgsConstructor
public class RedisGeneratorManage implements IdGeneratorManage {

	List<String> initIdListCache = new ArrayList<>();

	private final ValueOperations valueOperations;

	private final IdGeneraterProperties idGeneraterProperties;

	private final RedisLockService redisLockService;

	/**
	 * 生成1个id
	 * @param slotId
	 * @return
	 */
	@Override
	public long generate(int slotId) throws Throwable {
		List<Long> ids = redisLockService.executeWithLock(getIdLockKey(slotId),()-> genIds(slotId, 1));
		return !ids.isEmpty() ? ids.get(0) : -1;
	}

	/**
	 * 生成size个id
	 * @param slotId
	 * @param size
	 * @return
	 * @throws IdGeneratorException
	 */
	@Override
	public List<Long> generate(int slotId, int size) throws Throwable {
		if (size > idGeneraterProperties.maxSize) {
			IdGeneratorException exception = new IdGeneratorException(IdExceptionCodeE.SizeLimit);
			if (log.isErrorEnabled()) {
				log.error("generate error:{}", exception.toString());
			}
			throw exception;
		}
		return redisLockService.executeWithLock(getIdLockKey(slotId),()-> genIds(slotId, 1));
	}

	/**
	 * 路由规则
	 * @param type
	 * @return
	 * @throws IdGeneratorException
	 */
	@Override
	public int slotId(long type) throws IdGeneratorException {
		if (type > 0) {
			return (int) (type % idGeneraterProperties.slotCount);
		}
		else {
			IdGeneratorException exception = new IdGeneratorException(IdExceptionCodeE.IllegalType);
			if (log.isErrorEnabled()) {
				log.error("slotId error:{}", exception.toString());
			}
			throw exception;
		}
	}

	public List<Long> genIds(int slotId, int size) throws Throwable {
		// 返回结果
		List<Long> list = new ArrayList<Long>();

		if (size <= 0) {
			return list;
		}
		String idTypeKey = getIdTypeKey(slotId);

		// 是否需要初始化
		if (!initIdListCache.contains(idTypeKey)) {
			valueOperations.setIfAbsent(idTypeKey, String.valueOf(idGeneraterProperties.slotInitialValue));
			initIdListCache.add(idTypeKey);
		}

		// redis:curId+size
		long currentCurId = valueOperations.increment(idTypeKey, size);

		if (log.isDebugEnabled()) {
			log.debug("currentCurId:{}", currentCurId);
		}

		// 获取该槽位的起始值
		long initValue = idGeneraterProperties.slotInitialValue;
		if (log.isDebugEnabled()) {
			log.debug("initValue:{}", initValue);
		}

		// 小于起始值，认为redis值非法
		if (currentCurId <= initValue) {

			// 恢复初始值
			currentCurId = initValue;
			valueOperations.set(idTypeKey, String.valueOf(currentCurId));
		}

		// 批量获取
		if (size > 1) {

			// 先前的curId
			long oldCurId = currentCurId - size;

			// 组装返回值
			for (long id = oldCurId + 1; id <= currentCurId; id++) {
				list.add(id);
			}

		} else {
			// 组装返回值
			list.add(currentCurId);

		}

		return list;
	}

	public String getIdLockKey(int slotId) {
		return "IDGEN_LOCK:" + slotId;
	}

	private String getIdTypeKey(int slotId) {
		return "IDGEN:" + slotId;
	}

}
