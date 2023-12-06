package com.jwk.upms.base.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author jiwk
 * @since 2023-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLog extends Model<SysLog> {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 日志类型
     */
    private Byte logType;

    /**
     * 日志标题
     */
    private String logTitle;

    /**
     * 操作IP地址
     */
    private String remoteAddr;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 操作方式
     */
    private String method;

    /**
     * 操作提交的数据
     */
    private String params;

    /**
     * 执行时间
     */
    private Long time;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 1:成功，2:失败
     */
    private Byte status;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateTime
     */
    private Date updateTime;

    /**
     * createBy
     */
    private String createBy;

    /**
     * updateBy
     */
    private String updateBy;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
