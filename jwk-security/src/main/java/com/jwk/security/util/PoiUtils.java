package com.jwk.security.util;

import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class PoiUtils {
    public static String getCellValueByCell(Cell cell) {
        //判断是否为null或空串
        if (cell==null || "".equals(cell.toString().trim())) {
            return "";
        }
        String cellValue = "";
        int cellType=cell.getCellType();

        switch (cellType) {
            //字符串类型
            case Cell.CELL_TYPE_STRING:
                cellValue= cell.getStringCellValue().trim();
                cellValue= StringUtils.isEmpty(cellValue) ? "" : cellValue;
                break;
            //布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            //数值类型
            case Cell.CELL_TYPE_NUMERIC:
                //判断日期类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    cellValue = DateUtil.formatDateToStringYYYYMMDDHHMMSS(cell.getDateCellValue());
                } else {  //否
                    cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                evaluator.evaluateFormulaCell(cell);
                CellValue value = evaluator.evaluate(cell);
                cellValue = String.valueOf(value.getNumberValue());
                break;
            default: //其它类型，取空串吧
                cellValue = "";
                break;
        }
        return cellValue;
    }
}
