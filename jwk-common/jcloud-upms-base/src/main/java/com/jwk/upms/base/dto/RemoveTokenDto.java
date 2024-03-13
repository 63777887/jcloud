package com.jwk.upms.base.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 删除token
 * </p>
 *
 * @author jiwk
 * @since 2024-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveTokenDto {

    private static final long serialVersionUID = 1L;

    /**
     * 组织机构ID
     */
    private Long orgId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

}
