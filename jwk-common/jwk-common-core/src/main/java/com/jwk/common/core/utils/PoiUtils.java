package com.jwk.common.core.utils;

import com.alibaba.excel.util.StringUtils;
import java.text.DecimalFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 *
 */
public class PoiUtils {

  public static String getCellValueByCell(Cell cell) {
    //判断是否为null或空串
    if (cell == null || "".equals(cell.toString().trim())) {
      return "";
    }
    String cellValue = "";
    CellType cellType = cell.getCellType();

    switch (cellType) {
      //字符串类型
      case STRING:
        cellValue = cell.getStringCellValue().trim();
        cellValue = StringUtils.isEmpty(cellValue) ? "" : cellValue;
        break;
      //布尔类型
      case BOOLEAN:
        cellValue = String.valueOf(cell.getBooleanCellValue());
        break;
      //数值类型
      case NUMERIC:
        //判断日期类型
        if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
          cellValue = DateUtil.formatDateToStringYYYYMMDDHHMMSS(cell.getDateCellValue());
        } else {  //否
          cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
        }
        break;
      case FORMULA:
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper()
            .createFormulaEvaluator();
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
