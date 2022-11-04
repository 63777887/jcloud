package com.jwk.common.Idgenerater.validator;

import com.jwk.common.Idgenerater.constant.SlotConstant;
import java.util.List;

/**
 * @author Jiwk
 * @date 2022/11/2
 * @version 0.1.3
 * <p>
 * id校验
 */
public class IdValidator {

	/**
	 * 校验slotId槽位产生的id是否合法
	 * @param slotId
	 * @param id
	 * @return
	 */
	public static boolean isCorrect(int slotId, long id) {

		if (slotId < 0) {
			return false;
		} else {
			// 普通槽位起始值
			return id > SlotConstant.GENERAL_SLOT_INITIAL_VALUE
					&& id < SlotConstant.GENERAL_SLOT_MAX_VALUE;
		}
	}

	public static boolean isCorrect(int slotId, List<Long> ids) {
		for (Long id : ids) {
			if (!isCorrect(slotId, id)) {
				return false;
			}
		}

		return true;
	}

}
