package com.jwk.down.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * `sys_menu`
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * menuPid
     */
    private Long parentId;

    /**
     * menuName
     */
    private String menuName;

    /**
     * menuSort
     */
    private Integer sort;

    /**
     * url
     */
    private String url;

    /**
     * icon
     */
    private String icon;

    /**
     * level
     */
    private Integer level;

    /**
     * status
     */
    private Byte status;

    /**
     * hidden
     */
    private Byte hidden;

    /**
     * viewImport
     */
    private String viewImport;

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
