package com.jwk.upms.web.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jwk.common.core.excel.ExcelReq;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.utils.AssertUtil;
import com.jwk.common.prometheus.exception.PrometheusExceptionCodeE;
import com.jwk.common.security.annotation.Inner;
import com.jwk.upms.web.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2023-12-05
 */
@RestController
@RequestMapping("/excel")
@Slf4j
public class ExcelController {


    @PostMapping("/importExcel")
    @Inner
    public RestResponse importExcel(@RequestParam("file") MultipartFile file, @RequestPart("businessType") String businessType) {
        AssertUtil.isTrue(StrUtil.isNotBlank(businessType), "非法的导入类型");
        ExcelService excelService = getExcelService(businessType);
        return RestResponse.success(excelService.importData(file));
    }

    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response, @RequestBody ExcelReq excelReq) {
        ExcelService excelService = getExcelService(excelReq.getBusinessType());
        excelService.exportData(response, excelReq);
    }

    @GetMapping("/downloadExcelTemp")
    public void downloadExcelTemp(HttpServletResponse response, String businessType) {
        ExcelService excelService = getExcelService(businessType);
        excelService.downloadExcelTemp(response);
    }

    @NotNull
    private static ExcelService getExcelService(String businessType) {
        Map<String, ExcelService> excelServiceMap = SpringUtil.getBeansOfType(ExcelService.class);
        Optional<ExcelService> optional = excelServiceMap.values().stream()
                .filter(service -> service.support(businessType)).findFirst();
        if (!optional.isPresent()) {
            if (log.isErrorEnabled()) {
                log.error("ExcelService error , not registryService instance for {}", businessType);
            }
            throw new ServiceException(String.valueOf(PrometheusExceptionCodeE.NoRegistryServiceInstance.getErrCode()), PrometheusExceptionCodeE.NoRegistryServiceInstance.getErrMsg());
        }
        ExcelService excelService = optional.get();
        return excelService;
    }
}
