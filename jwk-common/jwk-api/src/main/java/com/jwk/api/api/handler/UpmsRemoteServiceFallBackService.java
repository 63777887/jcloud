package com.jwk.api.api.handler;

import com.jwk.api.api.UpmsRemoteService;
import com.jwk.api.dto.SysApiDto;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.core.model.InnerResponse;
import java.util.Collections;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class UpmsRemoteServiceFallBackService implements UpmsRemoteService {

  @Setter
  private Throwable cause;

  @Override
  public InnerResponse<UserInfo> findUserByName(String name) {
    log.error("feign findUserByName fail:{}", name, cause);
    return null;
  }

  @Override
  public InnerResponse<UserInfo> findUserByPhone(String phone) {
    log.error("feign findUserByPhone fail:{}", phone, cause);
    return null;
  }

  @Override
  public InnerResponse<List<SysApiDto>> resourceList() {
    log.error("feign resourceList fail:", cause);
    // 给一个默认没有的权限
    SysApiDto sysApiDto = new SysApiDto();
    sysApiDto.setUrl("FORBIDDEN");
    sysApiDto.setApiDesc("没有权限");
    return InnerResponse.success(Collections.singletonList(sysApiDto));
  }
}
