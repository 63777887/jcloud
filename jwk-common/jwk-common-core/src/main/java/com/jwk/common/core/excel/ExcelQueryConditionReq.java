package com.jwk.common.core.excel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * excel导出条件
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
