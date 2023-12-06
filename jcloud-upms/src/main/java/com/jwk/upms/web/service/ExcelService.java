package com.jwk.upms.web.service;

import com.jwk.common.core.excel.ExcelReq;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 导入导出
 * @date 2022/11/22
 */
public interface ExcelService {

    Boolean importData(MultipartFile file);

    Boolean support(String type);

    String getFileName();

    void exportData(HttpServletResponse response, ExcelReq excelReq);
}
