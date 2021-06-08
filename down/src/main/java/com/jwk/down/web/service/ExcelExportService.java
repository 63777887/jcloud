package com.jwk.down.web.service;


import com.jwk.down.exception.ServiceException;
import com.jwk.down.common.excel.ExcelExportReq;
import com.jwk.down.enums.ExportResourcesTypeReqE;
import com.jwk.down.enums.StatBusinessTypeReqE;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * All rights Reserved, Designed By SEGI
 * <pre>
 * Copyright:  Copyright(C) 2018
 * Company:    SEGI.
 * @Author: dengbp
 * @Date: 2018/8/21
 * </pre>
 * <p>
 *     excel导出业务服务接口
 * </p>
 */
public interface ExcelExportService {
    /**
     * 导出业务方法
     * @param condition condition
     * @return HSSFWorkbook
     * @throws ServiceException
     */
    HSSFWorkbook export( ExcelExportReq condition) throws ServiceException;

    /**
     * 生成excel的sheet名称
     * @param e 业务类型
     * @return sheet名称
     */
    default String generateSheetName(StatBusinessTypeReqE e) {
        return e.getName();
    }

    /**
     * 导出资源清单
     * 生成excel的sheet名称
     * @param e
     * @return
     */
    default String generateSheetName1(ExportResourcesTypeReqE e) {
        return e.getName();
    }


}
