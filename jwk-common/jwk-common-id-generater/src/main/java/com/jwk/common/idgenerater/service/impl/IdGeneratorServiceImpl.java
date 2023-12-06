package com.jwk.common.idgenerater.service.impl;

import com.jwk.common.idgenerater.exception.IdExceptionCodeE;
import com.jwk.common.idgenerater.exception.IdGeneratorException;
import com.jwk.common.idgenerater.manager.IdGeneratorManage;
import com.jwk.common.idgenerater.properties.IdGeneraterProperties;
import com.jwk.common.idgenerater.service.IdGeneratorService;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jiwk
 * @version 0.1.3
 * <p>
 * ID生成器服务
 * @date 2022/11/3
 */
@Slf4j
@RequiredArgsConstructor
public class IdGeneratorServiceImpl implements IdGeneratorService {

	private final IdGeneratorManage idGeneratorManage;

	private final IdGeneraterProperties idGeneraterProperties;

	/**
	 * 根据type获取一个id
	 * @param type
	 * @return
	 * @throws IdGeneratorException
	 */
	@Override
	public long getId(long type) throws Throwable {
		int slotId = idGeneratorManage.slotId(type);
		long id = idGeneratorManage.generate(slotId);
		if (isCorrect(slotId, id)) {
			if (log.isDebugEnabled()) {
				log.debug("id: {}", id);
			}
			return id;
		} else {
			IdGeneratorException exception = new IdGeneratorException(IdExceptionCodeE.NoUserFulId);
			if (log.isErrorEnabled()) {
				log.error("getId error:{}", exception.toString());
			}
			throw exception;
		}
	}

	/**
	 * 根据type获取一批id
	 * @param type
	 * @param size
	 * @return
	 * @throws IdGeneratorException
	 */
	@Override
	public List<Long> listIds(long type, int size) throws Throwable {
		int slotId = idGeneratorManage.slotId(type);
		List<Long> ids = idGeneratorManage.generate(slotId, size);
		if (isCorrect(slotId, ids)) {
			// 从小到大排序
			ids = ids.stream().distinct().sorted().collect(Collectors.toList());
			if (log.isDebugEnabled()) {
				log.debug("ids: {}", ids);
			}
			return ids;
		}
		else {
			IdGeneratorException exception = new IdGeneratorException(IdExceptionCodeE.NoUserFulId);
			if (log.isErrorEnabled()) {
				log.error("listIds error:{}", exception.toString());
			}
			throw exception;
		}
	}

	/**
	 * 校验slotId槽位产生的id是否合法
	 * @param slotId
	 * @param id
	 * @return
	 */
	private boolean isCorrect(int slotId, long id) {

		if (slotId < 0) {
			return false;
		}
		else {
			// 普通槽位起始值
			return id > idGeneraterProperties.slotInitialValue;
		}
	}

	private boolean isCorrect(int slotId, List<Long> ids) {
		for (Long id : ids) {
			if (!isCorrect(slotId, id)) {
				return false;
			}
		}

		return true;
	}

}
