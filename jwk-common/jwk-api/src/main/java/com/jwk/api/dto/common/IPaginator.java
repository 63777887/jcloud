package com.jwk.api.dto.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Springcloud返回分页对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IPaginator implements Serializable {

  private Integer pageLimit;
  private Integer pageNo;
  private Integer totalCount;
}
