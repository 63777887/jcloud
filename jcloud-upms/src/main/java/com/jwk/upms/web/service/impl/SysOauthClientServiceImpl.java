package com.jwk.upms.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jwk.upms.base.dto.SysOauthClientDto;
import com.jwk.upms.base.entity.SysOauthClient;
import com.jwk.upms.web.dao.SysOauthClientMapper;
import com.jwk.upms.base.entity.SysRole;
import com.jwk.upms.web.service.SysOauthClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwk.upms.web.service.SysRoleService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiwk
 * @since 2022-11-22
 */
@Service
public class SysOauthClientServiceImpl extends ServiceImpl<SysOauthClientMapper, SysOauthClient> implements SysOauthClientService {

  @Autowired
  SysRoleService sysRoleService;

  @Override
  public SysOauthClientDto getClientDetailsById(String clientId) {
    SysOauthClientDto  sysOauthClientDto= new SysOauthClientDto();
    SysOauthClient sysOauthClient = getOne(
        Wrappers.<SysOauthClient>lambdaQuery().eq(SysOauthClient::getClientId, clientId), false);
    String scope = sysOauthClient.getScope();
    if (StrUtil.isBlank(scope)){
      return sysOauthClientDto;
    }
    List<SysRole> list = sysRoleService.lambdaQuery().in(SysRole::getCode, scope.split(StrPool.COMMA)).list();
    BeanUtil.copyProperties(sysOauthClient,sysOauthClientDto);
    sysOauthClientDto.setScopeList(list);
    return sysOauthClientDto;
  }
}
