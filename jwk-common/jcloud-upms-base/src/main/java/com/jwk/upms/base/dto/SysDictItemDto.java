package com.jwk.upms.base.dto;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 字典项
 * </p>
 *
 * @author jiwk
 * @since 2024-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDictItemDto {

    private static final long serialVersionUID = 1L;

    /**
     * 字典ID
     */
    private Long dictId;

    /**
     * 字典项值
     */
    private String itemValue;

    /**
     * 字典项名称
     */
    private String label;

    /**
     * 字典类型
     */
    private List<String> dictTypes;
    private String dictType;

    /**
     * 排序（升序）
     */
    private Integer sortOrder;

    /**
     * 状态
     */
    private Byte status;

}
