package com.jwk.upms.base.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author jiwk
 * @since 2024-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDict extends Model<SysDict> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 描述
     */
    private String description;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 状态
     */
    private Byte status;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
