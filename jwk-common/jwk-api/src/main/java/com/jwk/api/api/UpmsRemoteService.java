package com.jwk.api.api;

import com.jwk.api.api.handler.UpmsRemoteServiceFallbackFactory;
import com.jwk.api.constant.ServerNameConstants;
import com.jwk.api.dto.SysApiDto;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.core.model.InnerResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Upms接口
 */
@FeignClient(name = ServerNameConstants.SERVER_UMPS,contextId = "authService",
    fallbackFactory = UpmsRemoteServiceFallbackFactory.class)
public interface UpmsRemoteService {

  /**
   * 根据姓名找用户
   * @param name
   * @return
   */
  @GetMapping("/inner/admin/findUserByName")
  InnerResponse<UserInfo> findUserByName(@RequestParam("name") String name);

  /**
   * 根据手机找用户
   * @param phone
   * @return
   */
  @GetMapping("/inner/admin/findUserByPhone")
  InnerResponse<UserInfo> findUserByPhone(@RequestParam("phone") String phone);

  /**
   * 获取资源列表
   * @return
   */
  @GetMapping("/inner/admin/resourceList")
  InnerResponse<List<SysApiDto>> resourceList();
}
