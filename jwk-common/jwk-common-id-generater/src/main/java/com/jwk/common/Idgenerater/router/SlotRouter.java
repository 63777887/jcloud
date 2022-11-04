package com.jwk.common.Idgenerater.router;

import com.jwk.common.Idgenerater.constant.SlotConstant;
import com.jwk.common.Idgenerater.exception.IdExceptionCodeE;
import com.jwk.common.Idgenerater.exception.IdGeneratorException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jiwk
 * @date 2022/11/2
 * @version 0.1.3
 * <p>
 * 路由
 */
@Slf4j
public class SlotRouter {

	/**
	 * 路由规则
	 * @param type
	 * @return
	 * @throws IdGeneratorException
	 */
	public static int slotId(long type) throws IdGeneratorException {
		if (type > 0) {
			return (int) (type % SlotConstant.GENERAL_SLOT_COUNT);
		} else {
			IdGeneratorException exception = new IdGeneratorException(IdExceptionCodeE.IllegalType);
			if (log.isErrorEnabled()){
				log.error("slotId error:{}", exception.toString());
			}
			throw exception;
		}
	}

}
