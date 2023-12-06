package com.jwk.common.core.excel;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * excel导出表头
 * @date 2022/6/11
 */
@Data
public class ExcelHeadReq implements Serializable {

	private static final long serialVersionUID = -7656126724185441059L;

	/**
	 * 排序
	 */
	private Integer order;

	/**
	 * 字段名
	 */
	private String field;

	/**
	 * 导出字段列名称
	 */
	private String fieldName;

	/**
	 * 二级表头
	 */
	private List<ExcelHeadReq> children;

}
