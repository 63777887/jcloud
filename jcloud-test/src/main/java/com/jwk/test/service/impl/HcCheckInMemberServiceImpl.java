package com.jwk.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.jwk.common.core.enums.StatBusinessTypeReqE;
import com.jwk.common.core.excel.ExcelReq;
import com.jwk.common.core.excel.ExcelHeadReq;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.test.service.ExcelExportService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * 入住人员管理Service
 */
@Service
public class HcCheckInMemberServiceImpl implements ExcelExportService {

	/**
	 * 导入条数限制
	 */
	public static final int IMPORT_LIMIT = 3000;

	/**
	 * 导入模板表头行数
	 */
	public static final int IMPORT_HEAD_LINE_NUM = 2;

	private final String TASK_NAME = "入住人员资质发起审批";

	@Override
	public HSSFWorkbook export(ExcelReq condition) throws ServiceException {
		// 根据reqList条件查询数据
		List<ExcelQueryConditionReq> reqList = condition.getReqList();
		Map<String, Object> map = new HashMap<>(reqList.size());
		if (CollectionUtils.isNotEmpty(reqList)) {
			for (ExcelQueryConditionReq req : reqList) {
				map.put(req.getCode(), req.getValue());
			}
		}
		else {
			return new HSSFWorkbook();
		}
		List<Object> resultList = new ArrayList<>();
		List<Map> dataList = JSON.parseArray(JSON.toJSONString(resultList), Map.class);

		// 创建excel
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(
				this.generateSheetName(StatBusinessTypeReqE.getById(Integer.valueOf(condition.getBusinessType()))));

		// 单元格锁定的样式
		HSSFCellStyle lockStyle = wb.createCellStyle();
		lockStyle.setLocked(true);

		// 标题行
		List<ExcelHeadReq> titleRow = condition.getExportList();

		if (CollectionUtils.isNotEmpty(resultList)) {
			HSSFCell cell = null;
			for (int i = 0; i < dataList.size() + 1; i++) {
				HSSFRow row = sheet.createRow(i);
				Map vo = null;
				if (i != 0) {
					vo = dataList.get(i - 1);
				}
				for (int j = 0; j < condition.getExportList().size(); j++) {
					ExcelHeadReq JCell = titleRow.get(j);
					if (i == 0) {
						row.createCell(j).setCellValue(JCell.getName());
					}
					if (i != 0) {
						String code = JCell.getCode();
						cell = row.createCell(j);
						Object value = vo.get(code);
						cell.setCellValue(value == null ? "" : value.toString());
						cell.setCellStyle(lockStyle);
					}
				}
			}
		}
		for (int i = 0; i < titleRow.size(); i++) {
			// 调整宽度
			sheet.setDefaultColumnWidth(15);
		}

		return wb;
	}

}
