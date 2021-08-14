package com.jwk.common.excel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelQueryConditionReq implements Serializable {

  private static final long serialVersionUID = -9136281407825878396L;

  /**
   * 条件编码
   */
  @NotBlank
  @Length(min = 1)
  private String code;
  /**
   * 对应编码值
   */
  private String value;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
