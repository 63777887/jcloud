package com.jwk.upms.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jwk.upms.base.dto.SysOauthClientDto;
import com.jwk.upms.base.entity.SysOauthClient;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jiwk
 * @since 2022-11-22
 */
public interface SysOauthClientService extends IService<SysOauthClient> {

	SysOauthClientDto getClientDetailsById(String clientId);

}
