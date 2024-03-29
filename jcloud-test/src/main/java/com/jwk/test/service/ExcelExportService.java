package com.jwk.test.service;

import com.jwk.common.core.enums.ExportResourcesTypeReqE;
import com.jwk.common.core.enums.StatBusinessTypeReqE;
import com.jwk.common.core.excel.ExcelReq;
import com.jwk.common.core.exception.ServiceException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * excel导出业务服务接口
 * </p>
 */
public interface ExcelExportService {

	/**
	 * 导出业务方法
	 * @param condition condition
	 * @return HSSFWorkbook
	 * @throws ServiceException
	 */
	HSSFWorkbook export(ExcelReq condition) throws ServiceException;

	/**
	 * 生成excel的sheet名称
	 * @param e 业务类型
	 * @return sheet名称
	 */
	default String generateSheetName(StatBusinessTypeReqE e) {
		return e.getName();
	}

	/**
	 * 导出资源清单 生成excel的sheet名称
	 * @param e
	 * @return
	 */
	default String generateSheetName1(ExportResourcesTypeReqE e) {
		return e.getName();
	}

}
