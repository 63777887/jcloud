package com.jwk.upms.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiwk
 * @since 2024-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysSetting extends Model<SysSetting> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 名称
     */
    private String paramName;

    /**
     * 键
     */
    private String paramKey;

    /**
     * 值，可为json串
     */
    private String paramValue;

    /**
     * 组织机构ID
     */
    private Long orgId;

    /**
     * 状态，1: 正常，2:删除
     */
    private Byte status;

    /**
     * 类型，0未知，1系统，2业务
     */
    private Byte paramType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
