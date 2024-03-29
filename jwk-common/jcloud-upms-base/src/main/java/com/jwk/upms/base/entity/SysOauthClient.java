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
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysOauthClient extends Model<SysOauthClient> {

	private static final long serialVersionUID = 1L;

	/**
	 * 客户端ID
	 */
	private String clientId;

	/**
	 * 客户端名称
	 */
	private String clientName;

	/**
	 * 资源列表
	 */
	private String resourceIds;

	/**
	 * 客户端密钥
	 */
	private String clientSecret;

	/**
	 * 域
	 */
	private String scope;

	/**
	 * 认证类型
	 */
	private String authorizedGrantTypes;

	/**
	 * 重定向地址
	 */
	private String webServerRedirectUri;

	/**
	 * token 有效期
	 */
	private Integer accessTokenValidity;

	/**
	 * 刷新令牌有效期
	 */
	private Integer refreshTokenValidity;

	/**
	 * 令牌扩展字段JSON
	 */
	private String additionalInformation;

	/**
	 * 是否自动放行
	 */
	private String autoapprove;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 更新人
	 */
	private String updateBy;

	@Override
	protected Serializable pkVal() {
		return this.clientId;
	}

}
