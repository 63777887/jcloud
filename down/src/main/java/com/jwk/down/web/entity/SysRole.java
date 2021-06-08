package com.jwk.down.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * `sys_role`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * roleName
     */
    private String roleName;

    /**
     * roleDesc
     */
    private String roleDesc;

    /**
     * roleCode
     */
    private String code;

    /**
     * roleSort
     */
    private Integer sort;

    /**
     * status
     */
    private Byte status;

    /**
     * createBy
     */
    private String createBy;

    /**
     * createTime
     */
    private Long createTime;

    /**
     * updateBy
     */
    private String updateBy;

    /**
     * updateTime
     */
    private Long updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
