package com.jwk.common.core.excel;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * excel导出表头
 */
@Data
public class ExcelHeadReq implements Serializable {

	private static final long serialVersionUID = -7656126724185441059L;

	/**
	 * 导出字段列编码
	 */
	private String code;

	/**
	 * 导出字段列名称
	 */
	private String name;

	/**
	 * 二级表头
	 */
	private List<ExcelHeadReq> children;

}
