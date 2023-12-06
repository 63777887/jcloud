package com.jwk.common.core.excel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * excel导出请求体
 * @date 2022/6/11
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelReq implements Serializable {

	private static final long serialVersionUID = -7618298119225197705L;

	/**
	 * 业务类型。
	 */
	@NotNull
	private String businessType;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 是否导出所有数据
	 */
	private Boolean isAll;

	/**
	 * 导出数据的参数
	 */
	private Map conditions;

	/**
	 * 表头集合
	 */
	@NotNull
	private List<String> exportFields;

}
