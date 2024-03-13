package com.jwk.upms.base.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class SysDictItem extends Model<SysDictItem> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

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
    private String dictType;

    /**
     * 字典项描述
     */
    private String description;

    /**
     * 排序（升序）
     */
    private Integer sortOrder;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * createBy
     */
    private String createBy;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateBy
     */
    private String updateBy;

    /**
     * updateTime
     */
    private Date updateTime;

    /**
     * 状态
     */
    private Byte status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
