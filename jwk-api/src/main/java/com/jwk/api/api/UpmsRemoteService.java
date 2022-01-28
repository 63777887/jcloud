package com.jwk.api.api;

import com.jwk.api.constant.ServerNameConstants;
import com.jwk.api.dto.SysApiDto;
import com.jwk.api.dto.UserInfo;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = ServerNameConstants.SERVER_UMPS,contextId = "authService")
public interface UpmsRemoteService {

  @GetMapping("/inner/admin/findUserByName")
  UserInfo findUserByName(@RequestParam String name);

  @GetMapping("/inner/admin/findUserByPhone")
  UserInfo findUserByPhone(@RequestParam String phone);

  @GetMapping("/inner/admin/resourceList")
  List<SysApiDto> resourceList();
}
