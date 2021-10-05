package com.jwk.common.core.excel;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

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
