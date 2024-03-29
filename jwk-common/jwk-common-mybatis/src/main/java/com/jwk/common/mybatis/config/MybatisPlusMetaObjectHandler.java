package com.jwk.common.mybatis.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.nio.charset.Charset;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ClassUtils;

/**
 * MybatisPlus 自动填充配置
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

	/**
	 * 填充值，先判断是否有手动设置，优先手动设置的值，例如：job必须手动设置
	 * @param fieldName 属性名
	 * @param fieldVal 属性值
	 * @param metaObject MetaObject
	 * @param isCover 是否覆盖原有值,避免更新操作手动入参
	 */
	private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
		// 1. 没有 get 方法
		if (!metaObject.hasSetter(fieldName)) {
			return;
		}
		// 2. 如果用户有手动设置的值
		Object userSetValue = metaObject.getValue(fieldName);
		String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
		if (StrUtil.isNotBlank(setValueStr) && !isCover) {
			return;
		}
		// 3. field 类型相同时设置
		Class<?> getterType = metaObject.getGetterType(fieldName);
		if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
			metaObject.setValue(fieldName, fieldVal);
		}
	}

	@Override
	public void insertFill(MetaObject metaObject) {
		if (log.isDebugEnabled()) {
			log.debug("mybatis plus start insert fill ....");
		}
		Date now = DateUtil.date();

		fillValIfNullByName("createTime", now, metaObject, false);
		fillValIfNullByName("updateTime", now, metaObject, false);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		if (log.isDebugEnabled()) {
			log.debug("mybatis plus start update fill ....");
		}
		fillValIfNullByName("updateTime", DateUtil.date(), metaObject, true);
	}

	// /**
	// * 获取 spring security 当前的用户名
	// * @return 当前用户名
	// */
	// private String getUserName() {
	// Authentication authentication =
	// SecurityContextHolder.getContext().getAuthentication();
	// if (Optional.ofNullable(authentication).isPresent()) {
	// return authentication.getName();
	// }
	// return null;
	// }

}
