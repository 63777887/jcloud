package com.jwk.api.dto.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Springcloud返回分页对象
 * @author guansc
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
