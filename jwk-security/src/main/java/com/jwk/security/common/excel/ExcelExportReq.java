package com.jwk.security.common.excel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By SEGI
 * <pre>
 * Copyright:  Copyright(C) 2018
 * Company:    SEGI.
 * @Author: dengbp
 * @Date: 2018/8/21
 * </pre>
 * <p>
 * </p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelExportReq implements Serializable {
    private static final long serialVersionUID = -7618298119225197705L;

    /**
     * 导出的业务类型。
     */
    @NotNull
    private String businessType;

    /**
     * 导出条件集合
     */
    @Valid
    @NotNull
    private List<ExcelQueryConditionReq> reqList;

    /**
     * 导出表头集合
     */
    @Valid
    @NotNull
    private List<ExcelHeadReq> exportList;

    private String cookie;

    public String getBusinessType() {
        return businessType;
    }
}
