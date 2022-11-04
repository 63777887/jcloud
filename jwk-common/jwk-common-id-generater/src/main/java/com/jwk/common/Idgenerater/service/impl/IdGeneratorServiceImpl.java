package com.jwk.common.Idgenerater.service.impl;

import com.jwk.common.Idgenerater.exception.IdExceptionCodeE;
import com.jwk.common.Idgenerater.exception.IdGeneratorException;
import com.jwk.common.Idgenerater.manager.IdGeneratorManage;
import com.jwk.common.Idgenerater.router.SlotRouter;
import com.jwk.common.Idgenerater.service.IdGeneratorService;
import com.jwk.common.Idgenerater.validator.IdValidator;
import com.jwk.common.redis.exception.RedisException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jiwk
 * @date 2022/11/3
 * @version 0.1.3
 * <p>
 * ID生成器服务
 */
@Service
@Slf4j
public class IdGeneratorServiceImpl implements IdGeneratorService {

	@Autowired
	private IdGeneratorManage idGeneratorManage;

	/**
	 * 根据type获取一个id
	 * @param type
	 * @return
	 * @throws IdGeneratorException
	 */
	@Override
	public long getId(long type) throws IdGeneratorException, RedisException {
		int slotId = SlotRouter.slotId(type);
		long id = idGeneratorManage.generate(slotId);
		if (IdValidator.isCorrect(slotId, id)) {
			if (log.isDebugEnabled()) {
				log.debug("id: {}",id);
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
	public List<Long> listIds(long type, int size) throws IdGeneratorException, RedisException {
		int slotId = SlotRouter.slotId(type);
		List<Long> ids = idGeneratorManage.generate(slotId, size);
		if (IdValidator.isCorrect(slotId, ids)) {
			// 从小到大排序
			ids = ids.stream().distinct().sorted().collect(Collectors.toList());
			if (log.isDebugEnabled()) {
				log.debug("ids: {}",ids);
			}
			return ids;
		} else {
			IdGeneratorException exception = new IdGeneratorException(IdExceptionCodeE.NoUserFulId);
			if (log.isErrorEnabled()) {
				log.error("listIds error:{}", exception.toString());
			}
			throw exception;
		}
	}
}
