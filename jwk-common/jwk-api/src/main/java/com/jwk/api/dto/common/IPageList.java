package com.jwk.api.dto.common;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Page对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IPageList<E> implements Serializable {

  private IPaginator paginator;

  private List<E> resultList;
}
