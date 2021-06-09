package com.jwk.security.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * `sys_org`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysOrg extends Model<SysOrg> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * orgPid
     */
    private Long parentId;

    /**
     * orgName
     */
    private String orgName;

    /**
     * address
     */
    private String address;

    /**
     * phone
     */
    private String phone;

    /**
     * email
     */
    private String email;

    /**
     * orgSort
     */
    private Integer orgSort;

    /**
     * level
     */
    private Integer level;

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
